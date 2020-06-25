import { doSpannerDml, doSpannerMutations, doSpannerSetup } from './music';
import { Spanner } from '@google-cloud/spanner';

const spanner = new Spanner({projectId: process.env.TEST_PROJECT_ID || 'helix-sydney'});
const instance = spanner.instance(process.env.TEST_INSTANCE_ID || 'test');
const db = instance.database(process.env.TEST_DATABASE_ID || 'test');

beforeEach(async () => {
  await doSpannerSetup(db);
});

test('DML', async () => {
  console.log('DML test');
  await doSpannerDml(db);
});

test('Mutations', async () => {
  console.log('Mutations test');
  await doSpannerMutations(db);
});

afterAll(async () => {
  await db.close();
});
