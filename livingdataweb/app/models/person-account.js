import BaseObject from 'livingdataweb/models/base-object';

export default BaseObject.extend({
  managedProperties:function(){
    return ['personName', 'currentBalance'];
  }
});
