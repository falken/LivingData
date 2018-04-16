import Controller from '@ember/controller';
import {inject as service} from '@ember/service';
import PersonData from 'livingdataweb/models/person-data'
import PersonAccount from 'livingdataweb/models/person-account';
import AccountTransaction from 'livingdataweb/models/account-transaction';
import ListResults from 'livingdataweb/models/list-results';
import {Promise} from 'rsvp';

export default Controller.extend({
  client: service('client'),
  isEditing:false,
  actions: {
    cancelEdit:function(){
      const controller = this;

      controller.set('isEditing',false);
    },
    toggleEdit: function(){
      const client = this.get('client');
      const controller = this;
      const model = controller.get('model');
      const meta = model.get('meta');
      const personId = model.get('personId');
      const currentlyEditing = controller.get('isEditing');
      if(currentlyEditing){
        client.patchResource('/api/people/' + personId,meta)
          .then(function(){
            return client.getResource('/api/people/' + personId)
              .then(function(data){
                const meta = PersonData.create();
                meta.loadFromData(data);
                model.set('meta',meta);
              });
          })
          .then(function(){
            controller.set('isEditing',false);
          });
      }else{
        controller.set('isEditing',true);
      }
    },
    postValue: function () {
      const controller = this;
      const client = this.get('client');
      const amount = this.get('amount');
      const model = this.get('model');
      const personId = model.get('meta.personId');
      const transaction = AccountTransaction.create();
      transaction.set('amount', amount);
      client.postResource('/api/people/' + personId + '/account/transactions', transaction)
        .then(function () {
          Promise.all([
            client.getResource('/api/people/' + personId + "/account"),
            client.getResource('/api/people/' + personId + "/account/transactions")
          ]).then(function (result) {
            const accountSummary = PersonAccount.create();
            accountSummary.loadFromData(result[0]);
            model.set('accountSummary', accountSummary);

            const accountTransactions = ListResults.create({childType: AccountTransaction});
            accountTransactions.loadFromData(result[1]);
            model.set('accountTransactions', accountTransactions);
            return model;
          }).then(function () {
            controller.set('amount', 0.0);
          });
        });
    }
  }
});
