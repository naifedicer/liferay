/*
Copyright (c) 2010, Yahoo! Inc. All rights reserved.
Code licensed under the BSD License:
http://developer.yahoo.com/yui/license.html
version: 3.2.0
build: nightly
*/
YUI.add("yql",function(B){var A=function(E,F,D,C){if(!D){D={};}D.q=E;if(!D.format){D.format=B.YQLRequest.FORMAT;}if(!D.env){D.env=B.YQLRequest.ENV;}this._params=D;this._opts=C;this._callback=F;};A.prototype={_opts:null,_callback:null,_params:null,send:function(){var C="",D=((this._opts&&this._opts.proto)?this._opts.proto:B.YQLRequest.PROTO);B.each(this._params,function(F,E){C+=E+"="+encodeURIComponent(F)+"&";});D+=((this._opts&&this._opts.base)?this._opts.base:B.YQLRequest.BASE_URL)+C;B.jsonp(D,this._callback);return this;}};A.FORMAT="json";A.PROTO="http";A.BASE_URL=":/"+"/query.yahooapis.com/v1/public/yql?";A.ENV="http:/"+"/datatables.org/alltables.env";B.YQLRequest=A;B.YQL=function(D,E,C){return new B.YQLRequest(D,E,C).send();};},"3.2.0",{requires:["jsonp"]});