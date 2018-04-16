/*
 * Manage - Studio Manager
 * Copyright (C) 2016 Iowa Ballet Academy
 */

import Service from '@ember/service';
import config from "../config/environment";
import Error from "livingdataweb/models/error";
import { Promise } from 'rsvp';
import $ from 'jquery';
import { isEmpty } from '@ember/utils';

export default Service.extend({
  apiUrl: null,
  buildUrl: function (urlPattern) {
    let apiUrl = this.get('apiUrl');
    if (isEmpty(apiUrl)) {
      apiUrl = config.APP.API_URL;
      this.set('apiUrl', apiUrl);
    }
    return apiUrl + urlPattern;
  },
  postResource: function (urlPattern, resource) {
    const client = this;

    return new Promise(function (resolve, reject) {
      if (!urlPattern) {
        reject(Error.create({errorMessage: "Invalid urlPattern"}));
        return;
      }
      $.ajax({
        url: client.buildUrl(urlPattern),
        async: true,
        type: 'POST',
        headers: {
          'Accept': 'application/json; charset=utf-8'
        },
        data: resource.toJson(),
        contentType: 'application/json; charset=utf-8',
      }).done(function () {
        resolve();
      }).fail(function (jqXHR, textStatus, errorThrown) {

          reject(Error.create({errorMessage: errorThrown, error: jqXHR.responseJSON}));
      });
    });
  },
  getResource: function (urlPattern) {
    const client = this;

    return new Promise(function (resolve, reject) {
      if (!urlPattern) {
        reject(Error.create({errorMessage: "Invalid urlPattern"}));
        return;
      }
      $.ajax({
        url: client.buildUrl(urlPattern),
        async: true,
        method: 'GET',
        headers: {
          'Accept': 'application/json; charset=utf-8'
        },
      }).done(function (data) {
        resolve(data);
      }).fail(function (jqXHR, textStatus, errorThrown) {
        if (textStatus === "error") {
          if (errorThrown === "Forbidden") {
            const value = jqXHR.responseJSON;
            reject(Error.create({errorMessage: 'Forbidden', description: value.description}));
          }
          else {
            reject(Error.create({errorMessage: 'Server Unavailable'}));
          }
        } else {
          reject(Error.create({errorMessage: errorThrown}));
        }
      });
    });
  },
  putResource: function (resource) {
    const client = this;

    return new Promise(function (resolve, reject) {

      $.ajax({
        url: client.buildUrl(resource.get('path')),
        async: true,
        type: 'PUT',
        data: resource.toJson(),
        contentType: 'application/json; charset=utf-8'
      }).done(function (data) {
        resolve(data);
      }).fail(function (jqXHR, textStatus, errorThrown) {
        if (textStatus === "error") {
          if (jqXHR.status) {
            const value = jqXHR.responseJSON;
            reject(Error.create({errorMessage: jqXHR.statusText, description: value.description}));
          } else {
            reject(Error.create({errorMessage: 'Server Unavailable'}));
          }
        } else {
          reject(Error.create({errorMessage: errorThrown}));
        }
      });
    });
  },
  patchResource: function (urlPattern,resource) {
    const client = this;

    return new Promise(function (resolve, reject) {

      $.ajax({
        url: client.buildUrl(urlPattern),
        async: true,
        type: 'PATCH',
        data: resource.toPatchJson(),
        contentType: 'application/json; charset=utf-8'
      }).done(function (data) {
        resolve(data);
      }).fail(function (jqXHR, textStatus, errorThrown) {
        if (textStatus === "error") {
          if (jqXHR.status) {
            const value = jqXHR.responseJSON;
            reject(Error.create({errorMessage: jqXHR.statusText, description: value.description}));
          } else {
            reject(Error.create({errorMessage: 'Server Unavailable'}));
          }
        } else {
          reject(Error.create({errorMessage: errorThrown}));
        }
      });
    });
  },
  deleteResourceByPath: function (urlPattern) {
    const client = this;

    return new Promise(function (resolve, reject) {

      $.ajax({
        url: client.buildUrl(urlPattern),
        async: true,
        type: 'DELETE',
        contentType: 'application/json; charset=utf-8'
      }).done(function (data) {
        resolve(data);
      }).fail(function (jqXHR, textStatus, errorThrown) {
        if (textStatus === "error") {
          if (jqXHR.status) {
            const value = jqXHR.responseJSON;
            reject(Error.create({errorMessage: jqXHR.statusText, description: value.description}));
          } else {
            reject(Error.create({errorMessage: 'Server Unavailable'}));
          }
        } else {
          reject(Error.create({errorMessage: errorThrown}));
        }
      });
    });
  }

});
