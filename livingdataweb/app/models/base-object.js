import Object from '@ember/object';
import { computed } from '@ember/object';

const emberToJsReducer = function(sourceObject){
  return (object, propertyName) => {
    object[propertyName] = sourceObject.get(propertyName);
    return object;
  };
};

const jsToEmberCopier = function(sourceObject,destinationObject){
  return (propertyName) => {
    destinationObject.set(propertyName,sourceObject[propertyName]);
  };
};

const managedObject = Object.extend({
  isNew: computed('path', function() {
    return (this.get('path') === null);
  }),
  toJson: function() {
    let managedProperties = this.managedProperties();
    return JSON.stringify(managedProperties.reduce(emberToJsReducer(this), {}));
  },
  loadFromData: function(data) {
    if(data.path){
      this.set('id', data.path.substring(data.path.lastIndexOf('/') + 1));
      this.set('path', data.path);
    }
    this.set('base_data',data);
    let managedProperties = this.managedProperties();

    if(managedProperties)
      managedProperties.forEach(jsToEmberCopier(data,this));
  },
  toPatchJson:function(){
      const sourceObject = this;
      const base_data = this.get('base_data');
      const managedProperties = this.managedProperties();
      const patchData = {};

      managedProperties.forEach(function(propertyName){
          const originalValue = base_data[propertyName];
          const newValue = sourceObject.get(propertyName);
          if(originalValue !== newValue){
            patchData[propertyName] = newValue;
          }
      });
      return JSON.stringify(patchData);
  }

});



managedObject.reopenClass({
  objectFromData: function (data) {
    if (data) {
      const object = this.create();
      object.loadFromData(data);
      return object;
    }
  }
});


export default managedObject;
