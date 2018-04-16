import Object from '@ember/object';
import { A } from '@ember/array';

export default Object.extend({
  init: function() {

    if (!this.get('results')) {
      this.set('results', A());
    }
    return this._super();
  },
  loadFromData: function(data) {
    const resultList = A();
    const childType = this.get('childType');
    if (data) {
      data.forEach(function(item) {
        const child = childType.create();
        child.loadFromData(item);
        resultList.addObject(child);
      });
    }
    this.set('results', resultList);
    this.set('hasItems', data ? data.length > 0 : false);
  },
  addItem: function(data) {
    const results = this.get('results');
    results.addObject(data);
  }
});
