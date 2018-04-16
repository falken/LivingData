import Route from '@ember/routing/route';
import Object from '@ember/object';
import { inject as service } from '@ember/service';
import PersonData from 'livingdataweb/models/person-data';
import PersonAccount from 'livingdataweb/models/person-account';
import AccountTransaction from 'livingdataweb/models/account-transaction';
import ListResults from 'livingdataweb/models/list-results';
import { Promise } from 'rsvp';

export default Route.extend({
  client: service('client'),
  model(params) {
    const client = this.get('client');
    const personId = params.person_id;

    return Promise.all([
      client.getResource('/api/people/' + personId),
      client.getResource('/api/people/' + personId + "/account"),
      client.getResource('/api/people/' + personId + "/account/transactions")
    ]).then(function(result){
      const model = Object.create();

      model.set('personId',personId);
      const meta = PersonData.create();
      meta.loadFromData(result[0]);
      model.set('meta',meta);

      const accountSummary = PersonAccount.create();
      accountSummary.loadFromData(result[1]);
      model.set('accountSummary',accountSummary);

      const accountTransactions = ListResults.create({childType:AccountTransaction});
      accountTransactions.loadFromData(result[2]);
      model.set('accountTransactions',accountTransactions);

      console.log(result);
      return model;
    });
  }
});
