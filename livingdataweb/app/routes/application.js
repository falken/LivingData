import Route from '@ember/routing/route';
import { inject as service } from '@ember/service';
import ListResults from 'livingdataweb/models/list-results';
import Person from 'livingdataweb/models/person';

export default Route.extend({
  client: service('client'),
  model() {
    const client = this.get('client');

    return client.getResource('/api/people')
      .then(function(data) {
        const results = ListResults.create({childType:Person});

        results.set('path','/api/people');
        results.set('client', client);
        results.loadFromData(data);

        return results;
      });
  }
});
