import { Database } from '@google-cloud/spanner';

async function tableExists(db: Database, table: string, sampleColumn: string): Promise<boolean> {
  try {
    await db.table(table).read({columns: [sampleColumn]});
    return true;
  } catch (notFoundError) {
    return false;
  }
}

async function createTablesIfNeeded(db: Database): Promise<void> {
  if (!tableExists(db, 'Singers', 'SingerId')) {
    const [,createOp] = await db.createTable(
      `CREATE TABLE Singers (
    SingerId   INT64 NOT NULL,
    FirstName  STRING(1024),
    LastName   STRING(1024),
    SingerInfo BYTES(MAX)"
) PRIMARY KEY (SingerId)"))
`
    );
    await createOp.promise();

    const [,createOp2] = await db.createTable(
      `CREATE TABLE Albums (
    SingerId     INT64 NOT NULL,
    AlbumId      INT64 NOT NULL,
    AlbumTitle   STRING(MAX)
) PRIMARY KEY (SingerId, AlbumId),
  INTERLEAVE IN PARENT Singers ON DELETE CASCADE"))
`
    );
    await createOp2.promise();

    console.log("Tables created");
  }

  await db.runTransactionAsync(async transaction => {
    await transaction.runUpdate('delete from Singers where SingerId > 0');
    await transaction.commit();
  });
}

export async function doSpannerSetup(db: Database): Promise<void> {
  await createTablesIfNeeded(db);
}

export async function doSpannerDml(db: Database): Promise<void> {
  await db.runTransactionAsync(async transaction => {
    await transaction.runUpdate({
      sql: 'insert into Singers(SingerId, FirstName, LastName) values (@singerId, @firstName, @lastName)',
      params: {
        SingerId: 10,
        FirstName: 'Floor',
        LastName: 'Jansen'
      }});
    await transaction.runUpdate({
      sql: 'insert into Singers(SingerId, FirstName, LastName) values (@singerId, @firstName, @lastName)',
      params: {
        SingerId: 20,
        FirstName: 'Charlotte',
        LastName: 'Wessels'
      }});

    await transaction.runUpdate({
      sql: 'insert into Albums (SingerId, AlbumId, AlbumTitle) values (@singerId, @albumId, @albumTitle)',
      params: {
        SingerId: 10,
        AlbumId: 1000,
        AlbumTitle: 'After Forever'
      }});
    await transaction.runUpdate({
      sql: 'insert into Albums (SingerId, AlbumId, AlbumTitle) values (@singerId, @albumId, @albumTitle)',
      params: {
        SingerId: 20,
        AlbumId: 2000,
        AlbumTitle: 'Lucidity'
      }});

    await transaction.commit();
  });

  const [rows] = await db.table('Singers').read({columns: ['FirstName', 'LastName']});
  for (const row of rows) {
    const rowData = row.toJSON();
    console.log('Singer:', rowData.FirstName, rowData.LastName);
  }
}

export async function doSpannerMutations(db: Database): Promise<void> {
  await db.runTransactionAsync(async transaction => {

    const singersTable = db.table('Singers');
    await singersTable.insert([
      {SingerId: 10, FirstName: 'Floor', LastName: 'Jansen'},
      {SingerId: 20, FirstName: 'Charlotte', LastName: 'Wessels'},
    ]);

    const albumsTable = db.table('Albums');
    await albumsTable.insert([
      {SingerId: 10, AlbumId: 1000, AlbumTitle: 'After Forever'},
      {SingerId: 20, AlbumId: 2000, AlbumTitle: 'Lucidity'},
    ]);
  });

  const [rows] = await db.table('Singers').read({columns: ['FirstName', 'LastName']});
  for (const row of rows) {
    const rowData = row.toJSON();
    console.log('Singer:', rowData.FirstName, rowData.LastName);
  }
}

