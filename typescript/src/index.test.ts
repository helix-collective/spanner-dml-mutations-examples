import { doSpannerDml, doSpannerMutations, doSpannerSetup } from './index';
import { Spanner } from '@google-cloud/spanner';

const spanner = new Spanner({projectId: 'helix-sydney'});
const instance = spanner.instance('prunge-test');
const db = instance.database('cats');

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
