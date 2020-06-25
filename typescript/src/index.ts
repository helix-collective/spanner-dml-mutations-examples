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
  if (!tableExists(db, 'Owners', 'OwnerId')) {
    const [,createOp] = await db.createTable(
      `CREATE TABLE Owners (
    OwnerId INT64 NOT NULL,
    OwnerName STRING(MAX) NOT NULL
) PRIMARY KEY (OwnerId)
`
    );
    await createOp.promise();

    const [,createOp2] = await db.createTable(
      `CREATE TABLE Cats (
    CatId INT64 NOT NULL,
    OwnerId INT64 NOT NULL,
    CatName String(MAX) NOT NULL
) 
PRIMARY KEY (OwnerId, CatId),
INTERLEAVE IN PARENT Owners ON DELETE CASCADE
`
    );
    await createOp2.promise();

    console.log("Tables created");
  }

  await db.runTransactionAsync(async transaction => {
    await transaction.runUpdate('delete from Owners where OwnerId > 0');
    await transaction.commit();
  });
}

export async function doSpannerSetup(db: Database): Promise<void> {
  await createTablesIfNeeded(db);
}

export async function doSpannerDml(db: Database): Promise<void> {
  await db.runTransactionAsync(async transaction => {
    await transaction.runUpdate({
      sql: 'insert into owners (OwnerId, OwnerName) values (@ownerId, @ownerName)',
      params: {
        ownerId: 1,
        ownerName: 'John Galah'
      }});
    await transaction.runUpdate({
      sql: 'insert into owners (OwnerId, OwnerName) values (@ownerId, @ownerName)',
      params: {
        ownerId: 2,
        ownerName: 'Jennifer Cockatoo'
      }});

    await transaction.runUpdate({
      sql: 'insert into cats (CatId, OwnerId, CatName) values (@catId, @ownerId, @catName)',
      params: {
        ownerId: 1,
        catId: 1000,
        catName: 'Dinah-Kah'
      }});
    await transaction.runUpdate({
      sql: 'insert into cats (CatId, OwnerId, CatName) values (@catId, @ownerId, @catName)',
      params: {
        ownerId: 2,
        catId: 1001,
        catName: 'Janvier'
      }});

    await transaction.commit();
  });

  const [rows] = await db.table('cats').read({columns: ['CatName']});
  for (const row of rows) {
    console.log('Cat:', row.toJSON().CatName);
  }
}

export async function doSpannerMutations(db: Database): Promise<void> {
  await db.runTransactionAsync(async transaction => {

    const ownerTable = db.table('owners');
    await ownerTable.insert([
      {ownerId: 1, ownerName: 'John Galah'},
      {ownerId: 2, ownerName: 'Jennifer Cockatoo'}
    ]);

    const catTable = db.table('cats');
    await catTable.insert([
      {ownerId: 1, catId: 1000, catName: 'Dinah-Kah'},
      {ownerId: 2, catId: 1001, catName: 'Janvier'},
    ]);
  });

  const [rows] = await db.table('cats').read({columns: ['CatName']});
  for (const row of rows) {
    console.log('Cat:', row.toJSON().CatName);
  }
}

