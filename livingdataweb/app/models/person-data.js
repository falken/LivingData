import BaseObject from 'livingdataweb/models/base-object';

export default BaseObject.extend({
  managedProperties:function(){
    return ['personName', 'personId','firstName','lastName','eyeColor','hairColor','height','weight','age'];
  },
  loadFromData: function(data) {
    this._super(data);
  }
});

