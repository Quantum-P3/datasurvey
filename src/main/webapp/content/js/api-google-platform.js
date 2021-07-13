var gapi = (window.gapi = window.gapi || {});
gapi._bs = new Date().getTime();
(function () {
  /*

 Copyright The Closure Library Authors.
 SPDX-License-Identifier: Apache-2.0
*/
  var n,
    aa = function (a) {
      var b = 0;
      return function () {
        return b < a.length ? { done: !1, value: a[b++] } : { done: !0 };
      };
    },
    ba =
      'function' == typeof Object.defineProperties
        ? Object.defineProperty
        : function (a, b, c) {
            if (a == Array.prototype || a == Object.prototype) return a;
            a[b] = c.value;
            return a;
          },
    ca = function (a) {
      a = [
        'object' == typeof globalThis && globalThis,
        a,
        'object' == typeof window && window,
        'object' == typeof self && self,
        'object' == typeof global && global,
      ];
      for (var b = 0; b < a.length; ++b) {
        var c = a[b];
        if (c && c.Math == Math) return c;
      }
      throw Error('Cannot find global object');
    },
    da = ca(this),
    ea = function (a, b) {
      if (b)
        a: {
          var c = da;
          a = a.split('.');
          for (var d = 0; d < a.length - 1; d++) {
            var e = a[d];
            if (!(e in c)) break a;
            c = c[e];
          }
          a = a[a.length - 1];
          d = c[a];
          b = b(d);
          b != d && null != b && ba(c, a, { configurable: !0, writable: !0, value: b });
        }
    };
  ea('Symbol', function (a) {
    if (a) return a;
    var b = function (f, g) {
      this.ba = f;
      ba(this, 'description', { configurable: !0, writable: !0, value: g });
    };
    b.prototype.toString = function () {
      return this.ba;
    };
    var c = 'jscomp_symbol_' + ((1e9 * Math.random()) >>> 0) + '_',
      d = 0,
      e = function (f) {
        if (this instanceof e) throw new TypeError('Symbol is not a constructor');
        return new b(c + (f || '') + '_' + d++, f);
      };
    return e;
  });
  ea('Symbol.iterator', function (a) {
    if (a) return a;
    a = Symbol('Symbol.iterator');
    for (
      var b = 'Array Int8Array Uint8Array Uint8ClampedArray Int16Array Uint16Array Int32Array Uint32Array Float32Array Float64Array'.split(
          ' '
        ),
        c = 0;
      c < b.length;
      c++
    ) {
      var d = da[b[c]];
      'function' === typeof d &&
        'function' != typeof d.prototype[a] &&
        ba(d.prototype, a, {
          configurable: !0,
          writable: !0,
          value: function () {
            return fa(aa(this));
          },
        });
    }
    return a;
  });
  var fa = function (a) {
      a = { next: a };
      a[Symbol.iterator] = function () {
        return this;
      };
      return a;
    },
    ja = function (a, b) {
      a instanceof String && (a += '');
      var c = 0,
        d = !1,
        e = {
          next: function () {
            if (!d && c < a.length) {
              var f = c++;
              return { value: b(f, a[f]), done: !1 };
            }
            d = !0;
            return { done: !0, value: void 0 };
          },
        };
      e[Symbol.iterator] = function () {
        return e;
      };
      return e;
    };
  ea('Array.prototype.keys', function (a) {
    return a
      ? a
      : function () {
          return ja(this, function (b) {
            return b;
          });
        };
  });
  var q = this || self,
    ka = function (a) {
      var b = typeof a;
      return 'object' != b ? b : a ? (Array.isArray(a) ? 'array' : b) : 'null';
    },
    la = function (a) {
      var b = ka(a);
      return 'array' == b || ('object' == b && 'number' == typeof a.length);
    },
    ma = function (a) {
      var b = typeof a;
      return ('object' == b && null != a) || 'function' == b;
    },
    na = function (a, b, c) {
      return a.call.apply(a.bind, arguments);
    },
    oa = function (a, b, c) {
      if (!a) throw Error();
      if (2 < arguments.length) {
        var d = Array.prototype.slice.call(arguments, 2);
        return function () {
          var e = Array.prototype.slice.call(arguments);
          Array.prototype.unshift.apply(e, d);
          return a.apply(b, e);
        };
      }
      return function () {
        return a.apply(b, arguments);
      };
    },
    pa = function (a, b, c) {
      pa = Function.prototype.bind && -1 != Function.prototype.bind.toString().indexOf('native code') ? na : oa;
      return pa.apply(null, arguments);
    },
    qa = function (a, b) {
      function c() {}
      c.prototype = b.prototype;
      a.na = b.prototype;
      a.prototype = new c();
      a.prototype.constructor = a;
      a.A = function (d, e, f) {
        for (var g = Array(arguments.length - 2), h = 2; h < arguments.length; h++) g[h - 2] = arguments[h];
        return b.prototype[e].apply(d, g);
      };
    },
    ra = function (a) {
      return a;
    },
    sa = function (a) {
      var b = null,
        c = q.trustedTypes;
      if (!c || !c.createPolicy) return b;
      try {
        b = c.createPolicy(a, { createHTML: ra, createScript: ra, createScriptURL: ra });
      } catch (d) {
        q.console && q.console.error(d.message);
      }
      return b;
    };
  function ta(a) {
    if (Error.captureStackTrace) Error.captureStackTrace(this, ta);
    else {
      var b = Error().stack;
      b && (this.stack = b);
    }
    a && (this.message = String(a));
  }
  qa(ta, Error);
  ta.prototype.name = 'CustomError';
  var ua;
  var va = function (a, b) {
    a = a.split('%s');
    for (var c = '', d = a.length - 1, e = 0; e < d; e++) c += a[e] + (e < b.length ? b[e] : '%s');
    ta.call(this, c + a[d]);
  };
  qa(va, ta);
  va.prototype.name = 'AssertionError';
  var wa = function (a, b, c, d) {
      var e = 'Assertion failed';
      if (c) {
        e += ': ' + c;
        var f = d;
      } else a && ((e += ': ' + a), (f = b));
      throw new va('' + e, f || []);
    },
    xa = function (a, b, c) {
      a || wa('', null, b, Array.prototype.slice.call(arguments, 2));
      return a;
    },
    ya = function (a, b) {
      throw new va('Failure' + (a ? ': ' + a : ''), Array.prototype.slice.call(arguments, 1));
    },
    za = function (a, b, c) {
      'string' !== typeof a && wa('Expected string but got %s: %s.', [ka(a), a], b, Array.prototype.slice.call(arguments, 2));
    };
  var Aa = Array.prototype.forEach
    ? function (a, b) {
        xa(null != a.length);
        Array.prototype.forEach.call(a, b, void 0);
      }
    : function (a, b) {
        for (var c = a.length, d = 'string' === typeof a ? a.split('') : a, e = 0; e < c; e++) e in d && b.call(void 0, d[e], e, a);
      };
  function Ba(a) {
    var b = a.length;
    if (0 < b) {
      for (var c = Array(b), d = 0; d < b; d++) c[d] = a[d];
      return c;
    }
    return [];
  }
  function Ca(a, b) {
    for (var c in a) b.call(void 0, a[c], c, a);
  }
  var Ea = 'constructor hasOwnProperty isPrototypeOf propertyIsEnumerable toLocaleString toString valueOf'.split(' ');
  function Fa(a, b) {
    for (var c, d, e = 1; e < arguments.length; e++) {
      d = arguments[e];
      for (c in d) a[c] = d[c];
      for (var f = 0; f < Ea.length; f++) (c = Ea[f]), Object.prototype.hasOwnProperty.call(d, c) && (a[c] = d[c]);
    }
  }
  var Ga;
  var r = function (a, b) {
    this.R = (a === Ha && b) || '';
    this.ca = Ia;
  };
  r.prototype.D = !0;
  r.prototype.C = function () {
    return this.R;
  };
  r.prototype.toString = function () {
    return 'Const{' + this.R + '}';
  };
  var Ja = function (a) {
      if (a instanceof r && a.constructor === r && a.ca === Ia) return a.R;
      ya("expected object of type Const, got '" + a + "'");
      return 'type_error:Const';
    },
    Ia = {},
    Ha = {};
  var Ka = /&/g,
    La = /</g,
    Ma = />/g,
    Na = /"/g,
    Oa = /'/g,
    Pa = /\x00/g,
    Qa = /[\x00&<>"']/,
    w = function (a, b) {
      return -1 != a.indexOf(b);
    };
  var x = function (a, b) {
    this.O = b === Ra ? a : '';
  };
  x.prototype.D = !0;
  x.prototype.C = function () {
    return this.O.toString();
  };
  x.prototype.toString = function () {
    return this.O.toString();
  };
  var Sa = function (a) {
      if (a instanceof x && a.constructor === x) return a.O;
      ya("expected object of type SafeUrl, got '" + a + "' of type " + ka(a));
      return 'type_error:SafeUrl';
    },
    Ta = /^(?:(?:https?|mailto|ftp):|[^:/?#]*(?:[/?#]|$))/i,
    Ua = function (a) {
      if (a instanceof x) return a;
      a = 'object' == typeof a && a.D ? a.C() : String(a);
      xa(Ta.test(a), '%s does not match the safe URL pattern', a) || (a = 'about:invalid#zClosurez');
      return new x(a, Ra);
    },
    Ra = {};
  var z;
  a: {
    var Va = q.navigator;
    if (Va) {
      var Wa = Va.userAgent;
      if (Wa) {
        z = Wa;
        break a;
      }
    }
    z = '';
  }
  var A = function (a, b, c) {
    this.N = c === Xa ? a : '';
  };
  A.prototype.D = !0;
  A.prototype.C = function () {
    return this.N.toString();
  };
  A.prototype.toString = function () {
    return this.N.toString();
  };
  var Ya = function (a) {
      if (a instanceof A && a.constructor === A) return a.N;
      ya("expected object of type SafeHtml, got '" + a + "' of type " + ka(a));
      return 'type_error:SafeHtml';
    },
    Xa = {},
    Za = new A((q.trustedTypes && q.trustedTypes.emptyHTML) || '', 0, Xa);
  var $a = function (a, b) {
    a: {
      try {
        var c = a && a.ownerDocument,
          d = c && (c.defaultView || c.parentWindow);
        d = d || q;
        if (d.Element && d.Location) {
          var e = d;
          break a;
        }
      } catch (g) {}
      e = null;
    }
    if (e && 'undefined' != typeof e[b] && (!a || (!(a instanceof e[b]) && (a instanceof e.Location || a instanceof e.Element)))) {
      if (ma(a))
        try {
          var f = a.constructor.displayName || a.constructor.name || Object.prototype.toString.call(a);
        } catch (g) {
          f = '<object could not be stringified>';
        }
      else f = void 0 === a ? 'undefined' : null === a ? 'null' : typeof a;
      ya('Argument is not a %s (or a non-Element, non-Location mock); got: %s', b, f);
    }
    return a;
  };
  var ab = { MATH: !0, SCRIPT: !0, STYLE: !0, SVG: !0, TEMPLATE: !0 },
    bb = (function (a) {
      var b = !1,
        c;
      return function () {
        b || ((c = a()), (b = !0));
        return c;
      };
    })(function () {
      if ('undefined' === typeof document) return !1;
      var a = document.createElement('div'),
        b = document.createElement('div');
      b.appendChild(document.createElement('div'));
      a.appendChild(b);
      if (!a.firstChild) return !1;
      b = a.firstChild.firstChild;
      a.innerHTML = Ya(Za);
      return !b.parentElement;
    });
  var cb = function (a) {
    Qa.test(a) &&
      (-1 != a.indexOf('&') && (a = a.replace(Ka, '&amp;')),
      -1 != a.indexOf('<') && (a = a.replace(La, '&lt;')),
      -1 != a.indexOf('>') && (a = a.replace(Ma, '&gt;')),
      -1 != a.indexOf('"') && (a = a.replace(Na, '&quot;')),
      -1 != a.indexOf("'") && (a = a.replace(Oa, '&#39;')),
      -1 != a.indexOf('\x00') && (a = a.replace(Pa, '&#0;')));
    return a;
  };
  var db = w(z, 'Opera'),
    eb = w(z, 'Trident') || w(z, 'MSIE'),
    fb = w(z, 'Edge'),
    hb = w(z, 'Gecko') && !(w(z.toLowerCase(), 'webkit') && !w(z, 'Edge')) && !(w(z, 'Trident') || w(z, 'MSIE')) && !w(z, 'Edge'),
    ib = w(z.toLowerCase(), 'webkit') && !w(z, 'Edge'),
    jb = function () {
      var a = q.document;
      return a ? a.documentMode : void 0;
    },
    kb;
  a: {
    var lb = '',
      mb = (function () {
        var a = z;
        if (hb) return /rv:([^\);]+)(\)|;)/.exec(a);
        if (fb) return /Edge\/([\d\.]+)/.exec(a);
        if (eb) return /\b(?:MSIE|rv)[: ]([^\);]+)(\)|;)/.exec(a);
        if (ib) return /WebKit\/(\S+)/.exec(a);
        if (db) return /(?:Version)[ \/]?(\S+)/.exec(a);
      })();
    mb && (lb = mb ? mb[1] : '');
    if (eb) {
      var nb = jb();
      if (null != nb && nb > parseFloat(lb)) {
        kb = String(nb);
        break a;
      }
    }
    kb = lb;
  }
  var ob = kb,
    pb;
  if (q.document && eb) {
    var qb = jb();
    pb = qb ? qb : parseInt(ob, 10) || void 0;
  } else pb = void 0;
  var rb = pb;
  var sb;
  (sb = !eb) || (sb = 9 <= Number(rb));
  var tb = sb;
  var vb = function (a, b) {
      Ca(b, function (c, d) {
        c && 'object' == typeof c && c.D && (c = c.C());
        'style' == d
          ? (a.style.cssText = c)
          : 'class' == d
          ? (a.className = c)
          : 'for' == d
          ? (a.htmlFor = c)
          : ub.hasOwnProperty(d)
          ? a.setAttribute(ub[d], c)
          : 0 == d.lastIndexOf('aria-', 0) || 0 == d.lastIndexOf('data-', 0)
          ? a.setAttribute(d, c)
          : (a[d] = c);
      });
    },
    ub = {
      cellpadding: 'cellPadding',
      cellspacing: 'cellSpacing',
      colspan: 'colSpan',
      frameborder: 'frameBorder',
      height: 'height',
      maxlength: 'maxLength',
      nonce: 'nonce',
      role: 'role',
      rowspan: 'rowSpan',
      type: 'type',
      usemap: 'useMap',
      valign: 'vAlign',
      width: 'width',
    },
    wb = function (a, b, c, d) {
      function e(h) {
        h && b.appendChild('string' === typeof h ? a.createTextNode(h) : h);
      }
      for (; d < c.length; d++) {
        var f = c[d];
        if (!la(f) || (ma(f) && 0 < f.nodeType)) e(f);
        else {
          a: {
            if (f && 'number' == typeof f.length) {
              if (ma(f)) {
                var g = 'function' == typeof f.item || 'string' == typeof f.item;
                break a;
              }
              if ('function' === typeof f) {
                g = 'function' == typeof f.item;
                break a;
              }
            }
            g = !1;
          }
          Aa(g ? Ba(f) : f, e);
        }
      }
    },
    xb = function (a, b) {
      b = String(b);
      'application/xhtml+xml' === a.contentType && (b = b.toLowerCase());
      return a.createElement(b);
    },
    yb = function (a) {
      xa(a, 'Node cannot be null or undefined.');
      return 9 == a.nodeType ? a : a.ownerDocument || a.document;
    },
    zb = function (a) {
      this.B = a || q.document || document;
    };
  n = zb.prototype;
  n.getElementsByTagName = function (a, b) {
    return (b || this.B).getElementsByTagName(String(a));
  };
  n.fa = function (a, b, c) {
    var d = this.B,
      e = arguments,
      f = String(e[0]),
      g = e[1];
    if (!tb && g && (g.name || g.type)) {
      f = ['<', f];
      g.name && f.push(' name="', cb(g.name), '"');
      if (g.type) {
        f.push(' type="', cb(g.type), '"');
        var h = {};
        Fa(h, g);
        delete h.type;
        g = h;
      }
      f.push('>');
      f = f.join('');
    }
    f = xb(d, f);
    g && ('string' === typeof g ? (f.className = g) : Array.isArray(g) ? (f.className = g.join(' ')) : vb(f, g));
    2 < e.length && wb(d, f, e, 2);
    return f;
  };
  n.createElement = function (a) {
    return xb(this.B, a);
  };
  n.createTextNode = function (a) {
    return this.B.createTextNode(String(a));
  };
  n.appendChild = function (a, b) {
    xa(null != a && null != b, 'goog.dom.appendChild expects non-null arguments');
    a.appendChild(b);
  };
  n.append = function (a, b) {
    wb(yb(a), a, arguments, 1);
  };
  n.canHaveChildren = function (a) {
    if (1 != a.nodeType) return !1;
    switch (a.tagName) {
      case 'APPLET':
      case 'AREA':
      case 'BASE':
      case 'BR':
      case 'COL':
      case 'COMMAND':
      case 'EMBED':
      case 'FRAME':
      case 'HR':
      case 'IMG':
      case 'INPUT':
      case 'IFRAME':
      case 'ISINDEX':
      case 'KEYGEN':
      case 'LINK':
      case 'NOFRAMES':
      case 'NOSCRIPT':
      case 'META':
      case 'OBJECT':
      case 'PARAM':
      case 'SCRIPT':
      case 'SOURCE':
      case 'STYLE':
      case 'TRACK':
      case 'WBR':
        return !1;
    }
    return !0;
  };
  n.removeNode = function (a) {
    return a && a.parentNode ? a.parentNode.removeChild(a) : null;
  };
  n.contains = function (a, b) {
    if (!a || !b) return !1;
    if (a.contains && 1 == b.nodeType) return a == b || a.contains(b);
    if ('undefined' != typeof a.compareDocumentPosition) return a == b || !!(a.compareDocumentPosition(b) & 16);
    for (; b && a != b; ) b = b.parentNode;
    return b == a;
  }; /*
 gapi.loader.OBJECT_CREATE_TEST_OVERRIDE &&*/
  var B = window,
    C = document,
    Ab = B.location,
    Bb = function () {},
    Cb = /\[native code\]/,
    D = function (a, b, c) {
      return (a[b] = a[b] || c);
    },
    Db = function (a) {
      for (var b = 0; b < this.length; b++) if (this[b] === a) return b;
      return -1;
    },
    Eb = function (a) {
      a = a.sort();
      for (var b = [], c = void 0, d = 0; d < a.length; d++) {
        var e = a[d];
        e != c && b.push(e);
        c = e;
      }
      return b;
    },
    Fb = /&/g,
    Gb = /</g,
    Hb = />/g,
    Ib = /"/g,
    Jb = /'/g,
    Kb = function (a) {
      return String(a).replace(Fb, '&amp;').replace(Gb, '&lt;').replace(Hb, '&gt;').replace(Ib, '&quot;').replace(Jb, '&#39;');
    },
    E = function () {
      var a;
      if ((a = Object.create) && Cb.test(a)) a = a(null);
      else {
        a = {};
        for (var b in a) a[b] = void 0;
      }
      return a;
    },
    F = function (a, b) {
      return Object.prototype.hasOwnProperty.call(a, b);
    },
    Lb = function (a) {
      if (Cb.test(Object.keys)) return Object.keys(a);
      var b = [],
        c;
      for (c in a) F(a, c) && b.push(c);
      return b;
    },
    G = function (a, b) {
      a = a || {};
      for (var c in a) F(a, c) && (b[c] = a[c]);
    },
    Mb = function (a) {
      return function () {
        B.setTimeout(a, 0);
      };
    },
    H = function (a, b) {
      if (!a) throw Error(b || '');
    },
    I = D(B, 'gapi', {});
  var J = function (a, b, c) {
      var d = new RegExp('([#].*&|[#])' + b + '=([^&#]*)', 'g');
      b = new RegExp('([?#].*&|[?#])' + b + '=([^&#]*)', 'g');
      if ((a = a && (d.exec(a) || b.exec(a))))
        try {
          c = decodeURIComponent(a[2]);
        } catch (e) {}
      return c;
    },
    Nb = new RegExp(
      /^/.source +
        /([a-zA-Z][-+.a-zA-Z0-9]*:)?/.source +
        /(\/\/[^\/?#]*)?/.source +
        /([^?#]*)?/.source +
        /(\?([^#]*))?/.source +
        /(#((#|[^#])*))?/.source +
        /$/.source
    ),
    Ob = /[\ud800-\udbff][\udc00-\udfff]|[^!-~]/g,
    Pb = new RegExp(
      /(%([^0-9a-fA-F%]|[0-9a-fA-F]([^0-9a-fA-F%])?)?)*/.source + /%($|[^0-9a-fA-F]|[0-9a-fA-F]($|[^0-9a-fA-F]))/.source,
      'g'
    ),
    Qb = /%([a-f]|[0-9a-fA-F][a-f])/g,
    Rb = /^(https?|ftp|file|chrome-extension):$/i,
    Sb = function (a) {
      a = String(a);
      a = a
        .replace(Ob, function (e) {
          try {
            return encodeURIComponent(e);
          } catch (f) {
            return encodeURIComponent(e.replace(/^[^%]+$/g, '\ufffd'));
          }
        })
        .replace(Pb, function (e) {
          return e.replace(/%/g, '%25');
        })
        .replace(Qb, function (e) {
          return e.toUpperCase();
        });
      a = a.match(Nb) || [];
      var b = E(),
        c = function (e) {
          return e
            .replace(/\\/g, '%5C')
            .replace(/\^/g, '%5E')
            .replace(/`/g, '%60')
            .replace(/\{/g, '%7B')
            .replace(/\|/g, '%7C')
            .replace(/\}/g, '%7D');
        },
        d = !!(a[1] || '').match(Rb);
      b.A = c((a[1] || '') + (a[2] || '') + (a[3] || (a[2] && d ? '/' : '')));
      d = function (e) {
        return c(e.replace(/\?/g, '%3F').replace(/#/g, '%23'));
      };
      b.query = a[5] ? [d(a[5])] : [];
      b.i = a[7] ? [d(a[7])] : [];
      return b;
    },
    Tb = function (a) {
      return a.A + (0 < a.query.length ? '?' + a.query.join('&') : '') + (0 < a.i.length ? '#' + a.i.join('&') : '');
    },
    Ub = function (a, b) {
      var c = [];
      if (a)
        for (var d in a)
          if (F(a, d) && null != a[d]) {
            var e = b ? b(a[d]) : a[d];
            c.push(encodeURIComponent(d) + '=' + encodeURIComponent(e));
          }
      return c;
    },
    Vb = function (a, b, c, d) {
      a = Sb(a);
      a.query.push.apply(a.query, Ub(b, d));
      a.i.push.apply(a.i, Ub(c, d));
      return Tb(a);
    },
    Wb = new RegExp(
      /\/?\??#?/.source +
        '(' +
        /[\/?#]/i.source +
        '|' +
        /[\uD800-\uDBFF]/i.source +
        '|' +
        /%[c-f][0-9a-f](%[89ab][0-9a-f]){0,2}(%[89ab]?)?/i.source +
        '|' +
        /%[0-9a-f]?/i.source +
        ')$',
      'i'
    ),
    Xb = function (a, b) {
      var c = Sb(b);
      b = c.A;
      c.query.length && (b += '?' + c.query.join(''));
      c.i.length && (b += '#' + c.i.join(''));
      var d = '';
      2e3 < b.length && ((d = b), (b = b.substr(0, 2e3)), (b = b.replace(Wb, '')), (d = d.substr(b.length)));
      var e = a.createElement('div');
      a = a.createElement('a');
      c = Sb(b);
      b = c.A;
      c.query.length && (b += '?' + c.query.join(''));
      c.i.length && (b += '#' + c.i.join(''));
      b = new x(b, Ra);
      $a(a, 'HTMLAnchorElement');
      b = b instanceof x ? b : Ua(b);
      a.href = Sa(b);
      e.appendChild(a);
      b = e.innerHTML;
      c = new r(Ha, 'Assignment to self.');
      za(Ja(c), 'must provide justification');
      xa(!/^[\s\xa0]*$/.test(Ja(c)), 'must provide non-empty justification');
      void 0 === Ga && (Ga = sa('gapi#html'));
      b = (c = Ga) ? c.createHTML(b) : b;
      b = new A(b, null, Xa);
      if (e.tagName && ab[e.tagName.toUpperCase()])
        throw Error('goog.dom.safe.setInnerHtml cannot be used to set content of ' + e.tagName + '.');
      if (bb()) for (; e.lastChild; ) e.removeChild(e.lastChild);
      e.innerHTML = Ya(b);
      b = String(e.firstChild.href);
      e.parentNode && e.parentNode.removeChild(e);
      c = Sb(b + d);
      d = c.A;
      c.query.length && (d += '?' + c.query.join(''));
      c.i.length && (d += '#' + c.i.join(''));
      return d;
    },
    Yb = /^https?:\/\/[^\/%\\?#\s]+\/[^\s]*$/i;
  var Zb;
  var $b = function (a, b, c, d) {
      if (B[c + 'EventListener']) B[c + 'EventListener'](a, b, !1);
      else if (B[d + 'tachEvent']) B[d + 'tachEvent']('on' + a, b);
    },
    ac = function () {
      var a = C.readyState;
      return 'complete' === a || ('interactive' === a && -1 == navigator.userAgent.indexOf('MSIE'));
    },
    dc = function (a) {
      var b = bc;
      if (!ac())
        try {
          b();
        } catch (c) {}
      cc(a);
    },
    cc = function (a) {
      if (ac()) a();
      else {
        var b = !1,
          c = function () {
            if (!b) return (b = !0), a.apply(this, arguments);
          };
        B.addEventListener
          ? (B.addEventListener('load', c, !1), B.addEventListener('DOMContentLoaded', c, !1))
          : B.attachEvent &&
            (B.attachEvent('onreadystatechange', function () {
              ac() && c.apply(this, arguments);
            }),
            B.attachEvent('onload', c));
      }
    },
    ec = function (a) {
      for (; a.firstChild; ) a.removeChild(a.firstChild);
    },
    fc = { button: !0, div: !0, span: !0 };
  var L;
  L = D(B, '___jsl', E());
  D(L, 'I', 0);
  D(L, 'hel', 10);
  var gc = function (a) {
      return L.dpo ? L.h : J(a, 'jsh', L.h);
    },
    hc = function (a) {
      var b = D(L, 'sws', []);
      b.push.apply(b, a);
    },
    ic = function (a) {
      return D(L, 'watt', E())[a];
    },
    jc = function (a) {
      var b = D(L, 'PQ', []);
      L.PQ = [];
      var c = b.length;
      if (0 === c) a();
      else
        for (
          var d = 0,
            e = function () {
              ++d === c && a();
            },
            f = 0;
          f < c;
          f++
        )
          b[f](e);
    },
    kc = function (a) {
      return D(D(L, 'H', E()), a, E());
    };
  var lc = D(L, 'perf', E()),
    mc = D(lc, 'g', E()),
    nc = D(lc, 'i', E());
  D(lc, 'r', []);
  E();
  E();
  var oc = function (a, b, c) {
      var d = lc.r;
      'function' === typeof d ? d(a, b, c) : d.push([a, b, c]);
    },
    N = function (a, b, c) {
      mc[a] = (!b && mc[a]) || c || new Date().getTime();
      oc(a);
    },
    qc = function (a, b, c) {
      b &&
        0 < b.length &&
        ((b = pc(b)),
        c && 0 < c.length && (b += '___' + pc(c)),
        28 < b.length && (b = b.substr(0, 28) + (b.length - 28)),
        (c = b),
        (b = D(nc, '_p', E())),
        (D(b, c, E())[a] = new Date().getTime()),
        oc(a, '_p', c));
    },
    pc = function (a) {
      return a.join('__').replace(/\./g, '_').replace(/\-/g, '_').replace(/,/g, '_');
    };
  var rc = E(),
    sc = [],
    O = function (a) {
      throw Error('Bad hint' + (a ? ': ' + a : ''));
    };
  sc.push([
    'jsl',
    function (a) {
      for (var b in a)
        if (F(a, b)) {
          var c = a[b];
          'object' == typeof c ? (L[b] = D(L, b, []).concat(c)) : D(L, b, c);
        }
      if ((b = a.u)) (a = D(L, 'us', [])), a.push(b), (b = /^https:(.*)$/.exec(b)) && a.push('http:' + b[1]);
    },
  ]);
  var tc = /^(\/[a-zA-Z0-9_\-]+)+$/,
    uc = [/\/amp\//, /\/amp$/, /^\/amp$/],
    vc = /^[a-zA-Z0-9\-_\.,!]+$/,
    wc = /^gapi\.loaded_[0-9]+$/,
    xc = /^[a-zA-Z0-9,._-]+$/,
    Bc = function (a, b, c, d, e) {
      var f = a.split(';'),
        g = f.shift(),
        h = rc[g],
        k = null;
      h ? (k = h(f, b, c, d)) : O('no hint processor for: ' + g);
      k || O('failed to generate load url');
      b = k;
      c = b.match(yc);
      ((d = b.match(zc)) && 1 === d.length && Ac.test(b) && c && 1 === c.length) || O('failed sanity: ' + a);
      try {
        e && 0 < e.length && (k = k + '?le=' + e.join(','));
      } catch (l) {}
      return k;
    },
    Ec = function (a, b, c, d) {
      a = Cc(a);
      wc.test(c) || O('invalid_callback');
      b = Dc(b);
      d = d && d.length ? Dc(d) : null;
      var e = function (f) {
        return encodeURIComponent(f).replace(/%2C/g, ',');
      };
      return [
        encodeURIComponent(a.pathPrefix).replace(/%2C/g, ',').replace(/%2F/g, '/'),
        '/k=',
        e(a.version),
        '/m=',
        e(b),
        d ? '/exm=' + e(d) : '',
        '/rt=j/sv=1/d=1/ed=1',
        a.T ? '/am=' + e(a.T) : '',
        a.Z ? '/rs=' + e(a.Z) : '',
        a.aa ? '/t=' + e(a.aa) : '',
        '/cb=',
        e(c),
      ].join('');
    },
    Cc = function (a) {
      '/' !== a.charAt(0) && O('relative path');
      for (var b = a.substring(1).split('/'), c = []; b.length; ) {
        a = b.shift();
        if (!a.length || 0 == a.indexOf('.')) O('empty/relative directory');
        else if (0 < a.indexOf('=')) {
          b.unshift(a);
          break;
        }
        c.push(a);
      }
      a = {};
      for (var d = 0, e = b.length; d < e; ++d) {
        var f = b[d].split('='),
          g = decodeURIComponent(f[0]),
          h = decodeURIComponent(f[1]);
        2 == f.length && g && h && (a[g] = a[g] || h);
      }
      b = '/' + c.join('/');
      tc.test(b) || O('invalid_prefix');
      c = 0;
      for (d = uc.length; c < d; ++c) uc[c].test(b) && O('invalid_prefix');
      c = Fc(a, 'k', !0);
      d = Fc(a, 'am');
      e = Fc(a, 'rs');
      a = Fc(a, 't');
      return { pathPrefix: b, version: c, T: d, Z: e, aa: a };
    },
    Dc = function (a) {
      for (var b = [], c = 0, d = a.length; c < d; ++c) {
        var e = a[c].replace(/\./g, '_').replace(/-/g, '_');
        xc.test(e) && b.push(e);
      }
      return b.join(',');
    },
    Fc = function (a, b, c) {
      a = a[b];
      !a && c && O('missing: ' + b);
      if (a) {
        if (vc.test(a)) return a;
        O('invalid: ' + b);
      }
      return null;
    },
    Ac = /^https?:\/\/[a-z0-9_.-]+\.google(rs)?\.com(:\d+)?\/[a-zA-Z0-9_.,!=\-\/]+$/,
    zc = /\/cb=/g,
    yc = /\/\//g,
    Gc = function () {
      var a = gc(Ab.href);
      if (!a) throw Error('Bad hint');
      return a;
    };
  rc.m = function (a, b, c, d) {
    (a = a[0]) || O('missing_hint');
    return 'https://apis.google.com' + Ec(a, b, c, d);
  };
  var Hc = decodeURI('%73cript'),
    Ic = /^[-+_0-9\/A-Za-z]+={0,2}$/,
    Jc = function (a, b) {
      for (var c = [], d = 0; d < a.length; ++d) {
        var e = a[d];
        e && 0 > Db.call(b, e) && c.push(e);
      }
      return c;
    },
    Kc = function () {
      var a = L.nonce;
      return void 0 !== a
        ? a && a === String(a) && a.match(Ic)
          ? a
          : (L.nonce = null)
        : C.querySelector
        ? (a = C.querySelector('script[nonce]'))
          ? ((a = a.nonce || a.getAttribute('nonce') || ''), a && a === String(a) && a.match(Ic) ? (L.nonce = a) : (L.nonce = null))
          : null
        : null;
    },
    Nc = function (a) {
      if ('loading' != C.readyState) Lc(a);
      else {
        var b = Kc(),
          c = '';
        null !== b && (c = ' nonce="' + b + '"');
        a = '<' + Hc + ' src="' + encodeURI(a) + '"' + c + '></' + Hc + '>';
        C.write(Mc ? Mc.createHTML(a) : a);
      }
    },
    Lc = function (a) {
      var b = C.createElement(Hc);
      b.setAttribute('src', Mc ? Mc.createScriptURL(a) : a);
      a = Kc();
      null !== a && b.setAttribute('nonce', a);
      b.async = 'true';
      (a = C.getElementsByTagName(Hc)[0]) ? a.parentNode.insertBefore(b, a) : (C.head || C.body || C.documentElement).appendChild(b);
    },
    Oc = function (a, b) {
      var c = b && b._c;
      if (c)
        for (var d = 0; d < sc.length; d++) {
          var e = sc[d][0],
            f = sc[d][1];
          f && F(c, e) && f(c[e], a, b);
        }
    },
    Qc = function (a, b, c) {
      Pc(function () {
        var d = b === gc(Ab.href) ? D(I, '_', E()) : E();
        d = D(kc(b), '_', d);
        a(d);
      }, c);
    },
    Sc = function (a, b) {
      var c = b || {};
      'function' == typeof b && ((c = {}), (c.callback = b));
      Oc(a, c);
      b = a ? a.split(':') : [];
      var d = c.h || Gc(),
        e = D(L, 'ah', E());
      if (e['::'] && b.length) {
        a = [];
        for (var f = null; (f = b.shift()); ) {
          var g = f.split('.');
          g = e[f] || e[(g[1] && 'ns:' + g[0]) || ''] || d;
          var h = (a.length && a[a.length - 1]) || null,
            k = h;
          (h && h.hint == g) || ((k = { hint: g, features: [] }), a.push(k));
          k.features.push(f);
        }
        var l = a.length;
        if (1 < l) {
          var m = c.callback;
          m &&
            (c.callback = function () {
              0 == --l && m();
            });
        }
        for (; (b = a.shift()); ) Rc(b.features, c, b.hint);
      } else Rc(b || [], c, d);
    },
    Rc = function (a, b, c) {
      a = Eb(a) || [];
      var d = b.callback,
        e = b.config,
        f = b.timeout,
        g = b.ontimeout,
        h = b.onerror,
        k = void 0;
      'function' == typeof h && (k = h);
      var l = null,
        m = !1;
      if ((f && !g) || (!f && g)) throw 'Timeout requires both the timeout parameter and ontimeout parameter to be set';
      h = D(kc(c), 'r', []).sort();
      var t = D(kc(c), 'L', []).sort(),
        u = L.le,
        p = [].concat(h),
        K = function (R, ha) {
          if (m) return 0;
          B.clearTimeout(l);
          t.push.apply(t, y);
          var ia = ((I || {}).config || {}).update;
          ia ? ia(e) : e && D(L, 'cu', []).push(e);
          if (ha) {
            qc('me0', R, p);
            try {
              Qc(ha, c, k);
            } finally {
              qc('me1', R, p);
            }
          }
          return 1;
        };
      0 < f &&
        (l = B.setTimeout(function () {
          m = !0;
          g();
        }, f));
      var y = Jc(a, t);
      if (y.length) {
        y = Jc(a, h);
        var v = D(L, 'CP', []),
          M = v.length;
        v[M] = function (R) {
          if (!R) return 0;
          qc('ml1', y, p);
          var ha = function (Da) {
              v[M] = null;
              K(y, R) &&
                jc(function () {
                  d && d();
                  Da();
                });
            },
            ia = function () {
              var Da = v[M + 1];
              Da && Da();
            };
          0 < M && v[M - 1]
            ? (v[M] = function () {
                ha(ia);
              })
            : ha(ia);
        };
        if (y.length) {
          var gb = 'loaded_' + L.I++;
          I[gb] = function (R) {
            v[M](R);
            I[gb] = null;
          };
          a = Bc(c, y, 'gapi.' + gb, h, u);
          h.push.apply(h, y);
          qc('ml0', y, p);
          b.sync || B.___gapisync ? Nc(a) : Lc(a);
        } else v[M](Bb);
      } else K(y) && d && d();
    },
    Mc = sa('gapi#gapi');
  var Pc = function (a, b) {
    if (L.hee && 0 < L.hel)
      try {
        return a();
      } catch (c) {
        b && b(c),
          L.hel--,
          Sc('debug_error', function () {
            try {
              window.___jsl.hefn(c);
            } catch (d) {
              throw c;
            }
          });
      }
    else
      try {
        return a();
      } catch (c) {
        throw (b && b(c), c);
      }
  };
  I.load = function (a, b) {
    return Pc(function () {
      return Sc(a, b);
    });
  };
  var Tc = function (a) {
      var b = (window.___jsl = window.___jsl || {});
      b[a] = b[a] || [];
      return b[a];
    },
    Uc = function (a) {
      var b = (window.___jsl = window.___jsl || {});
      b.cfg = (!a && b.cfg) || {};
      return b.cfg;
    },
    Vc = function (a) {
      return 'object' === typeof a && /\[native code\]/.test(a.push);
    },
    P = function (a, b, c) {
      if (b && 'object' === typeof b)
        for (var d in b)
          !Object.prototype.hasOwnProperty.call(b, d) ||
            (c && '___goc' === d && 'undefined' === typeof b[d]) ||
            (a[d] && b[d] && 'object' === typeof a[d] && 'object' === typeof b[d] && !Vc(a[d]) && !Vc(b[d])
              ? P(a[d], b[d])
              : b[d] && 'object' === typeof b[d]
              ? ((a[d] = Vc(b[d]) ? [] : {}), P(a[d], b[d]))
              : (a[d] = b[d]));
    },
    Wc = function (a) {
      if (a && !/^\s+$/.test(a)) {
        for (; 0 == a.charCodeAt(a.length - 1); ) a = a.substring(0, a.length - 1);
        try {
          var b = window.JSON.parse(a);
        } catch (c) {}
        if ('object' === typeof b) return b;
        try {
          b = new Function('return (' + a + '\n)')();
        } catch (c) {}
        if ('object' === typeof b) return b;
        try {
          b = new Function('return ({' + a + '\n})')();
        } catch (c) {}
        return 'object' === typeof b ? b : {};
      }
    },
    Xc = function (a, b) {
      var c = { ___goc: void 0 };
      a.length &&
        a[a.length - 1] &&
        Object.hasOwnProperty.call(a[a.length - 1], '___goc') &&
        'undefined' === typeof a[a.length - 1].___goc &&
        (c = a.pop());
      P(c, b);
      a.push(c);
    },
    Yc = function (a) {
      Uc(!0);
      var b = window.___gcfg,
        c = Tc('cu'),
        d = window.___gu;
      b && b !== d && (Xc(c, b), (window.___gu = b));
      b = Tc('cu');
      var e = document.scripts || document.getElementsByTagName('script') || [];
      d = [];
      var f = [];
      f.push.apply(f, Tc('us'));
      for (var g = 0; g < e.length; ++g) for (var h = e[g], k = 0; k < f.length; ++k) h.src && 0 == h.src.indexOf(f[k]) && d.push(h);
      0 == d.length && 0 < e.length && e[e.length - 1].src && d.push(e[e.length - 1]);
      for (e = 0; e < d.length; ++e)
        d[e].getAttribute('gapi_processed') ||
          (d[e].setAttribute('gapi_processed', !0),
          (f = d[e]) ? ((g = f.nodeType), (f = 3 == g || 4 == g ? f.nodeValue : f.textContent || '')) : (f = void 0),
          (f = Wc(f)) && b.push(f));
      a && Xc(c, a);
      d = Tc('cd');
      a = 0;
      for (b = d.length; a < b; ++a) P(Uc(), d[a], !0);
      d = Tc('ci');
      a = 0;
      for (b = d.length; a < b; ++a) P(Uc(), d[a], !0);
      a = 0;
      for (b = c.length; a < b; ++a) P(Uc(), c[a], !0);
    },
    Q = function (a) {
      var b = Uc();
      if (!a) return b;
      a = a.split('/');
      for (var c = 0, d = a.length; b && 'object' === typeof b && c < d; ++c) b = b[a[c]];
      return c === a.length && void 0 !== b ? b : void 0;
    },
    Zc = function (a, b) {
      var c;
      if ('string' === typeof a) {
        var d = (c = {});
        a = a.split('/');
        for (var e = 0, f = a.length; e < f - 1; ++e) {
          var g = {};
          d = d[a[e]] = g;
        }
        d[a[e]] = b;
      } else c = a;
      Yc(c);
    };
  var $c = function () {
    var a = window.__GOOGLEAPIS;
    a &&
      (a.googleapis && !a['googleapis.config'] && (a['googleapis.config'] = a.googleapis),
      D(L, 'ci', []).push(a),
      (window.__GOOGLEAPIS = void 0));
  };
  var ad = { callback: 1, clientid: 1, cookiepolicy: 1, openidrealm: -1, includegrantedscopes: -1, requestvisibleactions: 1, scope: 1 },
    bd = !1,
    cd = E(),
    dd = function () {
      if (!bd) {
        for (var a = document.getElementsByTagName('meta'), b = 0; b < a.length; ++b) {
          var c = a[b].name.toLowerCase();
          if (0 == c.lastIndexOf('google-signin-', 0)) {
            c = c.substring(14);
            var d = a[b].content;
            ad[c] && d && (cd[c] = d);
          }
        }
        if (window.self !== window.top) {
          a = document.location.toString();
          for (var e in ad) 0 < ad[e] && (b = J(a, e, '')) && (cd[e] = b);
        }
        bd = !0;
      }
      e = E();
      G(cd, e);
      return e;
    },
    ed = function (a) {
      return !!(a.clientid && a.scope && a.callback);
    };
  var fd = window.console,
    gd = function (a) {
      fd && fd.log && fd.log(a);
    };
  var hd = function () {
      return !!L.oa;
    },
    id = function () {};
  var S = D(L, 'rw', E()),
    jd = function (a) {
      for (var b in S) a(S[b]);
    },
    kd = function (a, b) {
      (a = S[a]) && a.state < b && (a.state = b);
    };
  var T = function (a) {
    var b = (window.___jsl = window.___jsl || {});
    b.cfg = b.cfg || {};
    b = b.cfg;
    if (!a) return b;
    a = a.split('/');
    for (var c = 0, d = a.length; b && 'object' === typeof b && c < d; ++c) b = b[a[c]];
    return c === a.length && void 0 !== b ? b : void 0;
  };
  var ld = /^https?:\/\/(?:\w|[\-\.])+\.google\.(?:\w|[\-:\.])+(?:\/[^\?#]*)?\/u\/(\d)\//,
    md = /^https?:\/\/(?:\w|[\-\.])+\.google\.(?:\w|[\-:\.])+(?:\/[^\?#]*)?\/b\/(\d{10,21})\//,
    nd = function (a) {
      var b = T('googleapis.config/sessionIndex');
      'string' === typeof b && 254 < b.length && (b = null);
      null == b && (b = window.__X_GOOG_AUTHUSER);
      'string' === typeof b && 254 < b.length && (b = null);
      if (null == b) {
        var c = window.google;
        c && (b = c.authuser);
      }
      'string' === typeof b && 254 < b.length && (b = null);
      null == b && ((a = a || window.location.href), (b = J(a, 'authuser') || null), null == b && (b = (b = a.match(ld)) ? b[1] : null));
      if (null == b) return null;
      b = String(b);
      254 < b.length && (b = null);
      return b;
    },
    od = function (a) {
      var b = T('googleapis.config/sessionDelegate');
      'string' === typeof b && 21 < b.length && (b = null);
      null == b && (b = (a = (a || window.location.href).match(md)) ? a[1] : null);
      if (null == b) return null;
      b = String(b);
      21 < b.length && (b = null);
      return b;
    };
  var pd,
    U,
    V = void 0,
    W = function (a) {
      try {
        return q.JSON.parse.call(q.JSON, a);
      } catch (b) {
        return !1;
      }
    },
    X = function (a) {
      return Object.prototype.toString.call(a);
    },
    qd = X(0),
    rd = X(new Date(0)),
    sd = X(!0),
    td = X(''),
    ud = X({}),
    vd = X([]),
    Y = function (a, b) {
      if (b) for (var c = 0, d = b.length; c < d; ++c) if (a === b[c]) throw new TypeError('Converting circular structure to JSON');
      d = typeof a;
      if ('undefined' !== d) {
        c = Array.prototype.slice.call(b || [], 0);
        c[c.length] = a;
        b = [];
        var e = X(a);
        if (
          null != a &&
          'function' === typeof a.toJSON &&
          (Object.prototype.hasOwnProperty.call(a, 'toJSON') ||
            ((e !== vd || (a.constructor !== Array && a.constructor !== Object)) &&
              (e !== ud || (a.constructor !== Array && a.constructor !== Object)) &&
              e !== td &&
              e !== qd &&
              e !== sd &&
              e !== rd))
        )
          return Y(a.toJSON.call(a), c);
        if (null == a) b[b.length] = 'null';
        else if (e === qd)
          (a = Number(a)), isNaN(a) || isNaN(a - a) ? (a = 'null') : -0 === a && 0 > 1 / a && (a = '-0'), (b[b.length] = String(a));
        else if (e === sd) b[b.length] = String(!!Number(a));
        else {
          if (e === rd) return Y(a.toISOString.call(a), c);
          if (e === vd && X(a.length) === qd) {
            b[b.length] = '[';
            var f = 0;
            for (d = Number(a.length) >> 0; f < d; ++f) f && (b[b.length] = ','), (b[b.length] = Y(a[f], c) || 'null');
            b[b.length] = ']';
          } else if (e == td && X(a.length) === qd) {
            b[b.length] = '"';
            f = 0;
            for (c = Number(a.length) >> 0; f < c; ++f)
              (d = String.prototype.charAt.call(a, f)),
                (e = String.prototype.charCodeAt.call(a, f)),
                (b[b.length] =
                  '\b' === d
                    ? '\\b'
                    : '\f' === d
                    ? '\\f'
                    : '\n' === d
                    ? '\\n'
                    : '\r' === d
                    ? '\\r'
                    : '\t' === d
                    ? '\\t'
                    : '\\' === d || '"' === d
                    ? '\\' + d
                    : 31 >= e
                    ? '\\u' + (e + 65536).toString(16).substr(1)
                    : 32 <= e && 65535 >= e
                    ? d
                    : '\ufffd');
            b[b.length] = '"';
          } else if ('object' === d) {
            b[b.length] = '{';
            d = 0;
            for (f in a)
              Object.prototype.hasOwnProperty.call(a, f) &&
                ((e = Y(a[f], c)),
                void 0 !== e && (d++ && (b[b.length] = ','), (b[b.length] = Y(f)), (b[b.length] = ':'), (b[b.length] = e)));
            b[b.length] = '}';
          } else return;
        }
        return b.join('');
      }
    },
    wd = /[\0-\x07\x0b\x0e-\x1f]/,
    xd = /^([^"]*"([^\\"]|\\.)*")*[^"]*"([^"\\]|\\.)*[\0-\x1f]/,
    yd = /^([^"]*"([^\\"]|\\.)*")*[^"]*"([^"\\]|\\.)*\\[^\\\/"bfnrtu]/,
    zd = /^([^"]*"([^\\"]|\\.)*")*[^"]*"([^"\\]|\\.)*\\u([0-9a-fA-F]{0,3}[^0-9a-fA-F])/,
    Ad = /"([^\0-\x1f\\"]|\\[\\\/"bfnrt]|\\u[0-9a-fA-F]{4})*"/g,
    Bd = /-?(0|[1-9][0-9]*)(\.[0-9]+)?([eE][-+]?[0-9]+)?/g,
    Cd = /[ \t\n\r]+/g,
    Dd = /[^"]:/,
    Ed = /""/g,
    Fd = /true|false|null/g,
    Gd = /00/,
    Hd = /[\{]([^0\}]|0[^:])/,
    Id = /(^|\[)[,:]|[,:](\]|\}|[,:]|$)/,
    Jd = /[^\[,:][\[\{]/,
    Kd = /^(\{|\}|\[|\]|,|:|0)+/,
    Ld = /\u2028/g,
    Md = /\u2029/g,
    Nd = function (a) {
      a = String(a);
      if (wd.test(a) || xd.test(a) || yd.test(a) || zd.test(a)) return !1;
      var b = a.replace(Ad, '""');
      b = b.replace(Bd, '0');
      b = b.replace(Cd, '');
      if (Dd.test(b)) return !1;
      b = b.replace(Ed, '0');
      b = b.replace(Fd, '0');
      if (Gd.test(b) || Hd.test(b) || Id.test(b) || Jd.test(b) || !b || (b = b.replace(Kd, ''))) return !1;
      a = a.replace(Ld, '\\u2028').replace(Md, '\\u2029');
      b = void 0;
      try {
        b = V ? [W(a)] : eval('(function (var_args) {\n  return Array.prototype.slice.call(arguments, 0);\n})(\n' + a + '\n)');
      } catch (c) {
        return !1;
      }
      return b && 1 === b.length ? b[0] : !1;
    },
    Od = function () {
      var a = ((q.document || {}).scripts || []).length;
      if ((void 0 === pd || void 0 === V || U !== a) && -1 !== U) {
        pd = V = !1;
        U = -1;
        try {
          try {
            V =
              !!q.JSON &&
              '{"a":[3,true,"1970-01-01T00:00:00.000Z"]}' ===
                q.JSON.stringify.call(q.JSON, { a: [3, !0, new Date(0)], c: function () {} }) &&
              !0 === W('true') &&
              3 === W('[{"a":3}]')[0].a;
          } catch (b) {}
          pd = V && !W('[00]') && !W('"\u0007"') && !W('"\\0"') && !W('"\\v"');
        } finally {
          U = a;
        }
      }
    },
    Pd = function (a) {
      if (-1 === U) return !1;
      Od();
      return (pd ? W : Nd)(a);
    },
    Qd = function (a) {
      if (-1 !== U) return Od(), V ? q.JSON.stringify.call(q.JSON, a) : Y(a);
    },
    Rd =
      !Date.prototype.toISOString ||
      'function' !== typeof Date.prototype.toISOString ||
      '1970-01-01T00:00:00.000Z' !== new Date(0).toISOString(),
    Sd = function () {
      var a = Date.prototype.getUTCFullYear.call(this);
      return [
        0 > a ? '-' + String(1e6 - a).substr(1) : 9999 >= a ? String(1e4 + a).substr(1) : '+' + String(1e6 + a).substr(1),
        '-',
        String(101 + Date.prototype.getUTCMonth.call(this)).substr(1),
        '-',
        String(100 + Date.prototype.getUTCDate.call(this)).substr(1),
        'T',
        String(100 + Date.prototype.getUTCHours.call(this)).substr(1),
        ':',
        String(100 + Date.prototype.getUTCMinutes.call(this)).substr(1),
        ':',
        String(100 + Date.prototype.getUTCSeconds.call(this)).substr(1),
        '.',
        String(1e3 + Date.prototype.getUTCMilliseconds.call(this)).substr(1),
        'Z',
      ].join('');
    };
  Date.prototype.toISOString = Rd ? Sd : Date.prototype.toISOString;
  var Td = function () {
    this.blockSize = -1;
  };
  var Ud = function () {
    this.blockSize = -1;
    this.blockSize = 64;
    this.g = [];
    this.K = [];
    this.da = [];
    this.G = [];
    this.G[0] = 128;
    for (var a = 1; a < this.blockSize; ++a) this.G[a] = 0;
    this.H = this.o = 0;
    this.reset();
  };
  qa(Ud, Td);
  Ud.prototype.reset = function () {
    this.g[0] = 1732584193;
    this.g[1] = 4023233417;
    this.g[2] = 2562383102;
    this.g[3] = 271733878;
    this.g[4] = 3285377520;
    this.H = this.o = 0;
  };
  var Vd = function (a, b, c) {
    c || (c = 0);
    var d = a.da;
    if ('string' === typeof b)
      for (var e = 0; 16 > e; e++)
        (d[e] = (b.charCodeAt(c) << 24) | (b.charCodeAt(c + 1) << 16) | (b.charCodeAt(c + 2) << 8) | b.charCodeAt(c + 3)), (c += 4);
    else for (e = 0; 16 > e; e++) (d[e] = (b[c] << 24) | (b[c + 1] << 16) | (b[c + 2] << 8) | b[c + 3]), (c += 4);
    for (e = 16; 80 > e; e++) {
      var f = d[e - 3] ^ d[e - 8] ^ d[e - 14] ^ d[e - 16];
      d[e] = ((f << 1) | (f >>> 31)) & 4294967295;
    }
    b = a.g[0];
    c = a.g[1];
    var g = a.g[2],
      h = a.g[3],
      k = a.g[4];
    for (e = 0; 80 > e; e++) {
      if (40 > e)
        if (20 > e) {
          f = h ^ (c & (g ^ h));
          var l = 1518500249;
        } else (f = c ^ g ^ h), (l = 1859775393);
      else 60 > e ? ((f = (c & g) | (h & (c | g))), (l = 2400959708)) : ((f = c ^ g ^ h), (l = 3395469782));
      f = (((b << 5) | (b >>> 27)) + f + k + l + d[e]) & 4294967295;
      k = h;
      h = g;
      g = ((c << 30) | (c >>> 2)) & 4294967295;
      c = b;
      b = f;
    }
    a.g[0] = (a.g[0] + b) & 4294967295;
    a.g[1] = (a.g[1] + c) & 4294967295;
    a.g[2] = (a.g[2] + g) & 4294967295;
    a.g[3] = (a.g[3] + h) & 4294967295;
    a.g[4] = (a.g[4] + k) & 4294967295;
  };
  Ud.prototype.update = function (a, b) {
    if (null != a) {
      void 0 === b && (b = a.length);
      for (var c = b - this.blockSize, d = 0, e = this.K, f = this.o; d < b; ) {
        if (0 == f) for (; d <= c; ) Vd(this, a, d), (d += this.blockSize);
        if ('string' === typeof a)
          for (; d < b; ) {
            if (((e[f] = a.charCodeAt(d)), ++f, ++d, f == this.blockSize)) {
              Vd(this, e);
              f = 0;
              break;
            }
          }
        else
          for (; d < b; )
            if (((e[f] = a[d]), ++f, ++d, f == this.blockSize)) {
              Vd(this, e);
              f = 0;
              break;
            }
      }
      this.o = f;
      this.H += b;
    }
  };
  Ud.prototype.digest = function () {
    var a = [],
      b = 8 * this.H;
    56 > this.o ? this.update(this.G, 56 - this.o) : this.update(this.G, this.blockSize - (this.o - 56));
    for (var c = this.blockSize - 1; 56 <= c; c--) (this.K[c] = b & 255), (b /= 256);
    Vd(this, this.K);
    for (c = b = 0; 5 > c; c++) for (var d = 24; 0 <= d; d -= 8) (a[b] = (this.g[c] >> d) & 255), ++b;
    return a;
  };
  var Wd = function () {
    this.P = new Ud();
  };
  Wd.prototype.reset = function () {
    this.P.reset();
  };
  var Xd = B.crypto,
    Yd = !1,
    Zd = 0,
    $d = 0,
    ae = 1,
    be = 0,
    ce = '',
    de = function (a) {
      a = a || B.event;
      var b = (a.screenX + a.clientX) << 16;
      b += a.screenY + a.clientY;
      b *= new Date().getTime() % 1e6;
      ae = (ae * b) % be;
      0 < Zd && ++$d == Zd && $b('mousemove', de, 'remove', 'de');
    },
    ee = function (a) {
      var b = new Wd();
      a = unescape(encodeURIComponent(a));
      for (var c = [], d = 0, e = a.length; d < e; ++d) c.push(a.charCodeAt(d));
      b.P.update(c);
      b = b.P.digest();
      a = '';
      for (c = 0; c < b.length; c++) a += '0123456789ABCDEF'.charAt(Math.floor(b[c] / 16)) + '0123456789ABCDEF'.charAt(b[c] % 16);
      return a;
    };
  Yd = !!Xd && 'function' == typeof Xd.getRandomValues;
  Yd ||
    ((be = 1e6 * (screen.width * screen.width + screen.height)),
    (ce = ee(C.cookie + '|' + C.location + '|' + new Date().getTime() + '|' + Math.random())),
    (Zd = T('random/maxObserveMousemove') || 0),
    0 != Zd && $b('mousemove', de, 'add', 'at'));
  var fe = function () {
      var a = L.onl;
      if (!a) {
        a = E();
        L.onl = a;
        var b = E();
        a.e = function (c) {
          var d = b[c];
          d && (delete b[c], d());
        };
        a.a = function (c, d) {
          b[c] = d;
        };
        a.r = function (c) {
          delete b[c];
        };
      }
      return a;
    },
    ge = function (a, b) {
      b = b.onload;
      return 'function' === typeof b ? (fe().a(a, b), b) : null;
    },
    he = function (a) {
      H(/^\w+$/.test(a), 'Unsupported id - ' + a);
      return 'onload="window.___jsl.onl.e(&#34;' + a + '&#34;)"';
    },
    ie = function (a) {
      fe().r(a);
    };
  var je = {
      allowtransparency: 'true',
      frameborder: '0',
      hspace: '0',
      marginheight: '0',
      marginwidth: '0',
      scrolling: 'no',
      style: '',
      tabindex: '0',
      vspace: '0',
      width: '100%',
    },
    ke = { allowtransparency: !0, onload: !0 },
    le = 0,
    me = function (a) {
      H(!a || Yb.test(a), 'Illegal url for new iframe - ' + a);
    },
    ne = function (a, b, c, d, e) {
      me(c.src);
      var f,
        g = ge(d, c),
        h = g ? he(d) : '';
      try {
        document.all &&
          (f = a.createElement(
            '<iframe frameborder="' +
              Kb(String(c.frameborder)) +
              '" scrolling="' +
              Kb(String(c.scrolling)) +
              '" ' +
              h +
              ' name="' +
              Kb(String(c.name)) +
              '"/>'
          ));
      } catch (l) {
      } finally {
        f ||
          ((f = (a ? new zb(yb(a)) : ua || (ua = new zb())).fa('IFRAME')),
          g &&
            ((f.onload = function () {
              f.onload = null;
              g.call(this);
            }),
            ie(d)));
      }
      f.setAttribute('ng-non-bindable', '');
      for (var k in c) (a = c[k]), 'style' === k && 'object' === typeof a ? G(a, f.style) : ke[k] || f.setAttribute(k, String(a));
      (k = (e && e.beforeNode) || null) || (e && e.dontclear) || ec(b);
      b.insertBefore(f, k);
      f = k ? k.previousSibling : b.lastChild;
      c.allowtransparency && (f.allowTransparency = !0);
      return f;
    };
  var oe = /^:[\w]+$/,
    pe = /:([a-zA-Z_]+):/g,
    qe = function () {
      var a = nd() || '0',
        b = od();
      var c = nd(void 0) || a;
      var d = od(void 0),
        e = '';
      c && (e += 'u/' + encodeURIComponent(String(c)) + '/');
      d && (e += 'b/' + encodeURIComponent(String(d)) + '/');
      c = e || null;
      (e = (d = !1 === T('isLoggedIn')) ? '_/im/' : '') && (c = '');
      var f = T('iframes/:socialhost:'),
        g = T('iframes/:im_socialhost:');
      return (Zb = { socialhost: f, ctx_socialhost: d ? g : f, session_index: a, session_delegate: b, session_prefix: c, im_prefix: e });
    },
    re = function (a, b) {
      return qe()[b] || '';
    },
    se = function (a) {
      return function (b, c) {
        return a ? qe()[c] || a[c] || '' : qe()[c] || '';
      };
    };
  var te = function (a) {
      var b;
      a.match(/^https?%3A/i) && (b = decodeURIComponent(a));
      return Xb(document, b ? b : a);
    },
    ue = function (a) {
      a = a || 'canonical';
      for (var b = document.getElementsByTagName('link'), c = 0, d = b.length; c < d; c++) {
        var e = b[c],
          f = e.getAttribute('rel');
        if (f && f.toLowerCase() == a && (e = e.getAttribute('href')) && (e = te(e)) && null != e.match(/^https?:\/\/[\w\-_\.]+/i))
          return e;
      }
      return window.location.href;
    };
  var ve = { se: '0' },
    we = { post: !0 },
    xe = { style: 'position:absolute;top:-10000px;width:450px;margin:0px;border-style:none' },
    ye = 'onPlusOne _ready _close _open _resizeMe _renderstart oncircled drefresh erefresh'.split(' '),
    ze = D(L, 'WI', E()),
    Ae = function (a, b, c) {
      var d;
      var e = {};
      var f = (d = a);
      'plus' == a && b.action && ((d = a + '_' + b.action), (f = a + '/' + b.action));
      (d = Q('iframes/' + d + '/url')) || (d = ':im_socialhost:/:session_prefix::im_prefix:_/widget/render/' + f + '?usegapi=1');
      for (var g in ve) e[g] = g + '/' + (b[g] || ve[g]) + '/';
      e = Xb(C, d.replace(pe, se(e)));
      g = 'iframes/' + a + '/params/';
      f = {};
      G(b, f);
      (d = Q('lang') || Q('gwidget/lang')) && (f.hl = d);
      we[a] || (f.origin = window.location.origin || window.location.protocol + '//' + window.location.host);
      f.exp = Q(g + 'exp');
      if ((g = Q(g + 'location')))
        for (d = 0; d < g.length; d++) {
          var h = g[d];
          f[h] = B.location[h];
        }
      switch (a) {
        case 'plus':
        case 'follow':
          g = f.href;
          d = b.action ? void 0 : 'publisher';
          g = (g = 'string' == typeof g ? g : void 0) ? te(g) : ue(d);
          f.url = g;
          delete f.href;
          break;
        case 'plusone':
          g = (g = b.href) ? te(g) : ue();
          f.url = g;
          g = b.db;
          d = Q();
          null == g && d && ((g = d.db), null == g && (g = d.gwidget && d.gwidget.db));
          f.db = g || void 0;
          g = b.ecp;
          d = Q();
          null == g && d && ((g = d.ecp), null == g && (g = d.gwidget && d.gwidget.ecp));
          f.ecp = g || void 0;
          delete f.href;
          break;
        case 'signin':
          f.url = ue();
      }
      L.ILI && (f.iloader = '1');
      delete f['data-onload'];
      delete f.rd;
      for (var k in ve) f[k] && delete f[k];
      f.gsrc = Q('iframes/:source:');
      k = Q('inline/css');
      'undefined' !== typeof k && 0 < c && k >= c && (f.ic = '1');
      k = /^#|^fr-/;
      c = {};
      for (var l in f) F(f, l) && k.test(l) && ((c[l.replace(k, '')] = f[l]), delete f[l]);
      l = 'q' == Q('iframes/' + a + '/params/si') ? f : c;
      k = dd();
      for (var m in k) !F(k, m) || F(f, m) || F(c, m) || (l[m] = k[m]);
      m = [].concat(ye);
      (l = Q('iframes/' + a + '/methods')) && 'object' === typeof l && Cb.test(l.push) && (m = m.concat(l));
      for (var t in b) F(b, t) && /^on/.test(t) && ('plus' != a || 'onconnect' != t) && (m.push(t), delete f[t]);
      delete f.callback;
      c._methods = m.join(',');
      return Vb(e, f, c);
    },
    Be = ['style', 'data-gapiscan'],
    De = function (a) {
      for (var b = E(), c = 0 != a.nodeName.toLowerCase().indexOf('g:'), d = 0, e = a.attributes.length; d < e; d++) {
        var f = a.attributes[d],
          g = f.name,
          h = f.value;
        0 <= Db.call(Be, g) ||
          (c && 0 != g.indexOf('data-')) ||
          'null' === h ||
          ('specified' in f && !f.specified) ||
          (c && (g = g.substr(5)), (b[g.toLowerCase()] = h));
      }
      a = a.style;
      (c = Ce(a && a.height)) && (b.height = String(c));
      (a = Ce(a && a.width)) && (b.width = String(a));
      return b;
    },
    Ce = function (a) {
      var b = void 0;
      'number' === typeof a ? (b = a) : 'string' === typeof a && (b = parseInt(a, 10));
      return b;
    },
    Fe = function () {
      var a = L.drw;
      jd(function (b) {
        if (a !== b.id && 4 != b.state && 'share' != b.type) {
          var c = b.id,
            d = b.type,
            e = b.url;
          b = b.userParams;
          var f = C.getElementById(c);
          if (f) {
            var g = Ae(d, b, 0);
            g
              ? ((f = f.parentNode),
                e.replace(/#.*/, '').replace(/(\?|&)ic=1/, '') !== g.replace(/#.*/, '').replace(/(\?|&)ic=1/, '') &&
                  ((b.dontclear = !0), (b.rd = !0), (b.ri = !0), (b.type = d), Ee(f, b), (d = S[f.lastChild.id]) && (d.oid = c), kd(c, 4)))
              : delete S[c];
          } else delete S[c];
        }
      });
    };
  var Ge,
    He,
    Ie,
    Je,
    Ke,
    Le = /(?:^|\s)g-((\S)*)(?:$|\s)/,
    Me = { plusone: !0, autocomplete: !0, profile: !0, signin: !0, signin2: !0 };
  Ge = D(L, 'SW', E());
  He = D(L, 'SA', E());
  Ie = D(L, 'SM', E());
  Je = D(L, 'FW', []);
  Ke = null;
  var Oe = function (a, b) {
      Ne(void 0, !1, a, b);
    },
    Ne = function (a, b, c, d) {
      N('ps0', !0);
      c = ('string' === typeof c ? document.getElementById(c) : c) || C;
      var e = C.documentMode;
      if (c.querySelectorAll && (!e || 8 < e)) {
        e = d ? [d] : Lb(Ge).concat(Lb(He)).concat(Lb(Ie));
        for (var f = [], g = 0; g < e.length; g++) {
          var h = e[g];
          f.push('.g-' + h, 'g\\:' + h);
        }
        e = c.querySelectorAll(f.join(','));
      } else e = c.getElementsByTagName('*');
      c = E();
      for (f = 0; f < e.length; f++) {
        g = e[f];
        var k = g;
        h = d;
        var l = k.nodeName.toLowerCase(),
          m = void 0;
        if (k.getAttribute('data-gapiscan')) h = null;
        else {
          var t = l.indexOf('g:');
          0 == t ? (m = l.substr(2)) : (t = (t = String(k.className || k.getAttribute('class'))) && Le.exec(t)) && (m = t[1]);
          h = !m || !(Ge[m] || He[m] || Ie[m]) || (h && m !== h) ? null : m;
        }
        h &&
          (Me[h] || 0 == g.nodeName.toLowerCase().indexOf('g:') || 0 != Lb(De(g)).length) &&
          (g.setAttribute('data-gapiscan', !0), D(c, h, []).push(g));
      }
      if (b) for (var u in c) for (b = c[u], d = 0; d < b.length; d++) b[d].setAttribute('data-onload', !0);
      for (var p in c) Je.push(p);
      N('ps1', !0);
      if ((u = Je.join(':')) || a)
        try {
          I.load(u, a);
        } catch (y) {
          gd(y);
          return;
        }
      if (Pe(Ke || {}))
        for (var K in c) {
          a = c[K];
          p = 0;
          for (b = a.length; p < b; p++) a[p].removeAttribute('data-gapiscan');
          Qe(K);
        }
      else {
        d = [];
        for (K in c) for (a = c[K], p = 0, b = a.length; p < b; p++) (e = a[p]), Re(K, e, De(e), d, b);
        Se(u, d);
      }
    },
    Te = function (a) {
      var b = D(I, a, {});
      b.go ||
        ((b.go = function (c) {
          return Oe(c, a);
        }),
        (b.render = function (c, d) {
          d = d || {};
          d.type = a;
          return Ee(c, d);
        }));
    },
    Ue = function (a) {
      Ge[a] = !0;
    },
    Ve = function (a) {
      He[a] = !0;
    },
    We = function (a) {
      Ie[a] = !0;
    };
  var Qe = function (a, b) {
      var c = ic(a);
      b && c
        ? (c(b), (c = b.iframeNode) && c.setAttribute('data-gapiattached', !0))
        : I.load(a, function () {
            var d = ic(a),
              e = b && b.iframeNode,
              f = b && b.userParams;
            e && d ? (d(b), e.setAttribute('data-gapiattached', !0)) : ((d = I[a].go), 'signin2' == a ? d(e, f) : d(e && e.parentNode, f));
          });
    },
    Pe = function () {
      return !1;
    },
    Se = function () {},
    Re = function (a, b, c, d, e, f, g) {
      switch (Xe(b, a, f)) {
        case 0:
          a = Ie[a] ? a + '_annotation' : a;
          d = {};
          d.iframeNode = b;
          d.userParams = c;
          Qe(a, d);
          break;
        case 1:
          if (b.parentNode) {
            for (var h in c) {
              if ((f = F(c, h)))
                (f = c[h]),
                  (f =
                    !!f &&
                    'object' === typeof f &&
                    (!f.toString || f.toString === Object.prototype.toString || f.toString === Array.prototype.toString));
              if (f)
                try {
                  c[h] = Qd(c[h]);
                } catch (M) {
                  delete c[h];
                }
            }
            f = !0;
            c.dontclear && (f = !1);
            delete c.dontclear;
            id();
            h = Ae(a, c, e);
            e = g || {};
            e.allowPost = 1;
            e.attributes = xe;
            e.dontclear = !f;
            g = {};
            g.userParams = c;
            g.url = h;
            g.type = a;
            if (c.rd) var k = b;
            else
              (k = document.createElement('div')),
                b.setAttribute('data-gapistub', !0),
                (k.style.cssText = 'position:absolute;width:450px;left:-10000px;'),
                b.parentNode.insertBefore(k, b);
            g.siteElement = k;
            k.id || ((b = k), D(ze, a, 0), (f = '___' + a + '_' + ze[a]++), (b.id = f));
            b = E();
            b['>type'] = a;
            G(c, b);
            f = h;
            c = k;
            h = e || {};
            b = h.attributes || {};
            H(!(h.allowPost || h.forcePost) || !b.onload, 'onload is not supported by post iframe (allowPost or forcePost)');
            e = b = f;
            oe.test(b) && ((e = T('iframes/' + e.substring(1) + '/url')), H(!!e, 'Unknown iframe url config for - ' + b));
            f = Xb(C, e.replace(pe, re));
            b = c.ownerDocument || C;
            e = h;
            var l = 0;
            do k = e.id || ['I', le++, '_', new Date().getTime()].join('');
            while (b.getElementById(k) && 5 > ++l);
            H(5 > l, 'Error creating iframe id');
            e = k;
            k = h;
            l = {};
            var m = {};
            b.documentMode && 9 > b.documentMode && (l.hostiemode = b.documentMode);
            G(k.queryParams || {}, l);
            G(k.fragmentParams || {}, m);
            var t = k.pfname;
            var u = E();
            T('iframes/dropLegacyIdParam') || (u.id = e);
            u._gfid = e;
            u.parent = b.location.protocol + '//' + b.location.host;
            var p = J(b.location.href, 'parent');
            t = t || '';
            !t &&
              p &&
              ((p = J(b.location.href, '_gfid', '') || J(b.location.href, 'id', '')),
              (t = J(b.location.href, 'pfname', '')),
              (t = p ? t + '/' + p : ''));
            t || ((p = Pd(J(b.location.href, 'jcp', ''))) && 'object' == typeof p && (t = (t = p.id) ? p.pfname + '/' + t : ''));
            u.pfname = t;
            k.connectWithJsonParam && ((p = {}), (p.jcp = Qd(u)), (u = p));
            p = J(f, 'rpctoken') || l.rpctoken || m.rpctoken;
            if (!p) {
              if (!(p = k.rpctoken)) {
                p = String;
                t = Math;
                var K = t.round;
                if (Yd) {
                  var y = new B.Uint32Array(1);
                  Xd.getRandomValues(y);
                  y = Number('0.' + y[0]);
                } else (y = ae), (y += parseInt(ce.substr(0, 20), 16)), (ce = ee(ce)), (y /= be + Math.pow(16, 20));
                p = p(K.call(t, 1e8 * y));
              }
              u.rpctoken = p;
            }
            k.rpctoken = p;
            G(u, k.connectWithQueryParams ? l : m);
            p = b.location.href;
            u = E();
            (t = J(p, '_bsh', L.bsh)) && (u._bsh = t);
            (p = gc(p)) && (u.jsh = p);
            k.hintInFragment ? G(u, m) : G(u, l);
            l = Vb(f, l, m, k.paramsSerializer);
            f = h;
            m = E();
            G(je, m);
            G(f.attributes, m);
            m.name = m.id = e;
            m.src = l;
            h.eurl = l;
            h = (k = h) || {};
            f = !!h.allowPost;
            if (h.forcePost || (f && 2e3 < l.length)) {
              f = Sb(l);
              m.src = '';
              k.dropDataPostorigin || (m['data-postorigin'] = l);
              h = ne(b, c, m, e);
              if (-1 != navigator.userAgent.indexOf('WebKit')) {
                var v = h.contentWindow.document;
                v.open();
                l = v.createElement('div');
                m = {};
                u = e + '_inner';
                m.name = u;
                m.src = '';
                m.style = 'display:none';
                ne(b, l, m, u, k);
              }
              l = (k = f.query[0]) ? k.split('&') : [];
              k = [];
              for (m = 0; m < l.length; m++) (u = l[m].split('=', 2)), k.push([decodeURIComponent(u[0]), decodeURIComponent(u[1])]);
              f.query = [];
              l = Tb(f);
              H(Yb.test(l), 'Invalid URL: ' + l);
              f = b.createElement('form');
              f.method = 'POST';
              f.target = e;
              f.style.display = 'none';
              e = l instanceof x ? l : Ua(l);
              $a(f, 'HTMLFormElement').action = Sa(e);
              for (e = 0; e < k.length; e++)
                (l = b.createElement('input')), (l.type = 'hidden'), (l.name = k[e][0]), (l.value = k[e][1]), f.appendChild(l);
              c.appendChild(f);
              f.submit();
              f.parentNode.removeChild(f);
              v && v.close();
              v = h;
            } else v = ne(b, c, m, e, k);
            g.iframeNode = v;
            g.id = v.getAttribute('id');
            v = g.id;
            c = E();
            c.id = v;
            c.userParams = g.userParams;
            c.url = g.url;
            c.type = g.type;
            c.state = 1;
            S[v] = c;
            v = g;
          } else v = null;
          v && ((g = v.id) && d.push(g), Qe(a, v));
      }
    },
    Xe = function (a, b, c) {
      if (a && 1 === a.nodeType && b) {
        if (c) return 1;
        if (Ie[b]) {
          if (fc[a.nodeName.toLowerCase()]) return (a = a.innerHTML) && a.replace(/^[\s\xa0]+|[\s\xa0]+$/g, '') ? 0 : 1;
        } else {
          if (He[b]) return 0;
          if (Ge[b]) return 1;
        }
      }
      return null;
    },
    Ee = function (a, b) {
      var c = b.type;
      delete b.type;
      var d = ('string' === typeof a ? document.getElementById(a) : a) || void 0;
      if (d) {
        a = {};
        for (var e in b) F(b, e) && (a[e.toLowerCase()] = b[e]);
        a.rd = 1;
        (b = !!a.ri) && delete a.ri;
        e = [];
        Re(c, d, a, e, 0, b, void 0);
        Se(c, e);
      } else gd('string' === 'gapi.' + c + '.render: missing element ' + typeof a ? a : '');
    };
  D(I, 'platform', {}).go = Oe;
  Pe = function (a) {
    for (var b = ['_c', 'jsl', 'h'], c = 0; c < b.length && a; c++) a = a[b[c]];
    b = gc(Ab.href);
    return !a || (0 != a.indexOf('n;') && 0 != b.indexOf('n;') && a !== b);
  };
  Se = function (a, b) {
    Ye(a, b);
  };
  var bc = function (a) {
      Ne(a, !0);
    },
    Ze = function (a, b) {
      b = b || [];
      for (var c = 0; c < b.length; ++c) a(b[c]);
      for (a = 0; a < b.length; a++) Te(b[a]);
    };
  sc.push([
    'platform',
    function (a, b, c) {
      Ke = c;
      b && Je.push(b);
      Ze(Ue, a);
      Ze(Ve, c._c.annotation);
      Ze(We, c._c.bimodal);
      $c();
      Yc();
      if ('explicit' != Q('parsetags')) {
        hc(a);
        ed(dd()) && !Q('disableRealtimeCallback') && id();
        if (c && (a = c.callback)) {
          var d = Mb(a);
          delete c.callback;
        }
        dc(function () {
          bc(d);
        });
      }
    },
  ]);
  I._pl = !0;
  var $e = function (a) {
    a = (a = S[a]) ? a.oid : void 0;
    if (a) {
      var b = C.getElementById(a);
      b && b.parentNode.removeChild(b);
      delete S[a];
      $e(a);
    }
  };
  var af = /^\{h:'/,
    bf = /^!_/,
    cf = '',
    Ye = function (a, b) {
      function c() {
        $b('message', d, 'remove', 'de');
      }
      function d(f) {
        var g = f.data,
          h = f.origin;
        if (df(g, b)) {
          var k = e;
          e = !1;
          k && N('rqe');
          ef(a, function () {
            k && N('rqd');
            c();
            for (var l = D(L, 'RPMQ', []), m = 0; m < l.length; m++) l[m]({ data: g, origin: h });
          });
        }
      }
      if (0 !== b.length) {
        cf = J(Ab.href, 'pfname', '');
        var e = !0;
        $b('message', d, 'add', 'at');
        Sc(a, c);
      }
    },
    df = function (a, b) {
      a = String(a);
      if (af.test(a)) return !0;
      var c = !1;
      bf.test(a) && ((c = !0), (a = a.substr(2)));
      if (!/^\{/.test(a)) return !1;
      var d = Pd(a);
      if (!d) return !1;
      a = d.f;
      if (d.s && a && -1 != Db.call(b, a)) {
        if ('_renderstart' === d.s || d.s === cf + '/' + a + '::_renderstart')
          if (((d = d.a && d.a[c ? 0 : 1]), (b = C.getElementById(a)), kd(a, 2), d && b && d.width && d.height)) {
            a: {
              c = b.parentNode;
              a = d || {};
              if (hd()) {
                var e = b.id;
                if (e) {
                  d = (d = S[e]) ? d.state : void 0;
                  if (1 === d || 4 === d) break a;
                  $e(e);
                }
              }
              (d = c.nextSibling) &&
                d.getAttribute &&
                d.getAttribute('data-gapistub') &&
                (c.parentNode.removeChild(d), (c.style.cssText = ''));
              d = a.width;
              var f = a.height,
                g = c.style;
              g.textIndent = '0';
              g.margin = '0';
              g.padding = '0';
              g.background = 'transparent';
              g.borderStyle = 'none';
              g.cssFloat = 'none';
              g.styleFloat = 'none';
              g.lineHeight = 'normal';
              g.fontSize = '1px';
              g.verticalAlign = 'baseline';
              c = c.style;
              c.display = 'inline-block';
              g = b.style;
              g.position = 'static';
              g.left = '0';
              g.top = '0';
              g.visibility = 'visible';
              d && (c.width = g.width = d + 'px');
              f && (c.height = g.height = f + 'px');
              a.verticalAlign && (c.verticalAlign = a.verticalAlign);
              e && kd(e, 3);
            }
            b['data-csi-wdt'] = new Date().getTime();
          }
        return !0;
      }
      return !1;
    },
    ef = function (a, b) {
      Sc(a, b);
    };
  var ff = function (a, b) {
    this.M = a;
    a = b || {};
    this.ga = Number(a.maxAge) || 0;
    this.V = a.domain;
    this.X = a.path;
    this.ha = !!a.secure;
  };
  ff.prototype.read = function () {
    for (var a = this.M + '=', b = document.cookie.split(/;\s*/), c = 0; c < b.length; ++c) {
      var d = b[c];
      if (0 == d.indexOf(a)) return d.substr(a.length);
    }
  };
  ff.prototype.write = function (a, b) {
    if (!gf.test(this.M)) throw 'Invalid cookie name';
    if (!hf.test(a)) throw 'Invalid cookie value';
    a = this.M + '=' + a;
    this.V && (a += ';domain=' + this.V);
    this.X && (a += ';path=' + this.X);
    b = 'number' === typeof b ? b : this.ga;
    if (0 <= b) {
      var c = new Date();
      c.setSeconds(c.getSeconds() + b);
      a += ';expires=' + c.toUTCString();
    }
    this.ha && (a += ';secure');
    document.cookie = a;
    return !0;
  };
  ff.prototype.clear = function () {
    this.write('', 0);
  };
  var hf = /^[-+/_=.:|%&a-zA-Z0-9@]*$/,
    gf = /^[A-Z_][A-Z0-9_]{0,63}$/;
  ff.iterate = function (a) {
    for (var b = document.cookie.split(/;\s*/), c = 0; c < b.length; ++c) {
      var d = b[c].split('='),
        e = d.shift();
      a(e, d.join('='));
    }
  };
  var jf = function (a) {
    this.F = a;
  };
  jf.prototype.read = function () {
    if (Z.hasOwnProperty(this.F)) return Z[this.F];
  };
  jf.prototype.write = function (a) {
    Z[this.F] = a;
    return !0;
  };
  jf.prototype.clear = function () {
    delete Z[this.F];
  };
  var Z = {};
  jf.iterate = function (a) {
    for (var b in Z) Z.hasOwnProperty(b) && a(b, Z[b]);
  };
  var kf = 'https:' === window.location.protocol,
    lf = kf || 'http:' === window.location.protocol ? ff : jf,
    mf = function (a) {
      var b = a.substr(1),
        c = '',
        d = window.location.hostname;
      if ('' !== b) {
        c = parseInt(b, 10);
        if (isNaN(c)) return null;
        b = d.split('.');
        if (b.length < c - 1) return null;
        b.length == c - 1 && (d = '.' + d);
      } else d = '';
      return { j: 'S' == a.charAt(0), domain: d, l: c };
    },
    nf = function () {
      var a,
        b = null;
      lf.iterate(function (c, d) {
        0 === c.indexOf('G_AUTHUSER_') &&
          ((c = mf(c.substring(11))), !a || (c.j && !a.j) || (c.j == a.j && c.l > a.l)) &&
          ((a = c), (b = d));
      });
      return { ea: a, J: b };
    };
  function of(a) {
    if (0 !== a.indexOf('GCSC')) return null;
    var b = { W: !1 };
    a = a.substr(4);
    if (!a) return b;
    var c = a.charAt(0);
    a = a.substr(1);
    var d = a.lastIndexOf('_');
    if (-1 == d) return b;
    var e = mf(a.substr(d + 1));
    if (null == e) return b;
    a = a.substring(0, d);
    if ('_' !== a.charAt(0)) return b;
    d = 'E' === c && e.j;
    return (!d && ('U' !== c || e.j)) || (d && !kf) ? b : { W: !0, j: d, ka: a.substr(1), domain: e.domain, l: e.l };
  }
  var pf = function (a) {
      if (!a) return [];
      a = a.split('=');
      return a[1] ? a[1].split('|') : [];
    },
    qf = function (a) {
      a = a.split(':');
      return { clientId: a[0].split('=')[1], ja: pf(a[1]), ma: pf(a[2]), la: pf(a[3]) };
    },
    rf = function () {
      var a = nf(),
        b = a.ea;
      a = a.J;
      if (null !== a) {
        var c;
        lf.iterate(function (f, g) {
          (f = of(f)) && f.W && f.j == b.j && f.l == b.l && (c = g);
        });
        if (c) {
          var d = qf(c),
            e = d && d.ja[Number(a)];
          d = d && d.clientId;
          if (e) return { J: a, ia: e, clientId: d };
        }
      }
      return null;
    };
  var tf = function () {
    this.U = sf;
  };
  n = tf.prototype;
  n.$ = function () {
    this.L || ((this.v = 0), (this.L = !0), this.Y());
  };
  n.Y = function () {
    this.L &&
      (this.U() ? (this.v = this.S) : (this.v = Math.min(2 * (this.v || this.S), 120)), window.setTimeout(pa(this.Y, this), 1e3 * this.v));
  };
  n.v = 0;
  n.S = 2;
  n.U = null;
  n.L = !1;
  for (var uf = 0; 64 > uf; ++uf);
  var vf = null;
  hd = function () {
    return (L.oa = !0);
  };
  id = function () {
    L.oa = !0;
    var a = rf();
    (a = a && a.J) && Zc('googleapis.config/sessionIndex', a);
    vf || (vf = D(L, 'ss', new tf()));
    a = vf;
    a.$ && a.$();
  };
  var sf = function () {
    var a = rf(),
      b = (a && a.ia) || null,
      c = a && a.clientId;
    Sc('auth', {
      callback: function () {
        var d = B.gapi.auth,
          e = { client_id: c, session_state: b };
        d.checkSessionState(e, function (f) {
          var g = e.session_state,
            h = !!Q('isLoggedIn');
          f = Q('debug/forceIm') ? !1 : (g && f) || (!g && !f);
          if ((h = h !== f)) Zc('isLoggedIn', f), id(), Fe(), f || ((f = d.signOut) ? f() : (f = d.setToken) && f(null));
          f = dd();
          var k = Q('savedUserState');
          g = d._guss(f.cookiepolicy);
          k = k != g && 'undefined' != typeof k;
          Zc('savedUserState', g);
          (h || k) && ed(f) && !Q('disableRealtimeCallback') && d._pimf(f, !0);
        });
      },
    });
    return !0;
  };
  N('bs0', !0, window.gapi._bs);
  N('bs1', !0);
  delete window.gapi._bs;
}.call(this));
gapi.load('', {
  callback: window['gapi_onload'],
  _c: {
    jsl: {
      ci: {
        deviceType: 'desktop',
        'oauth-flow': {
          authUrl: 'https://accounts.google.com/o/oauth2/auth',
          proxyUrl: 'https://accounts.google.com/o/oauth2/postmessageRelay',
          disableOpt: true,
          idpIframeUrl: 'https://accounts.google.com/o/oauth2/iframe',
          usegapi: false,
        },
        debug: { reportExceptionRate: 0.05, forceIm: false, rethrowException: false, host: 'https://apis.google.com' },
        enableMultilogin: true,
        'googleapis.config': { auth: { useFirstPartyAuthV2: true } },
        isPlusUser: false,
        inline: { css: 1 },
        disableRealtimeCallback: false,
        drive_share: { skipInitCommand: true },
        csi: { rate: 0.01 },
        client: { cors: false },
        signInDeprecation: { rate: 0.0 },
        include_granted_scopes: true,
        llang: 'en',
        iframes: {
          youtube: {
            params: { location: ['search', 'hash'] },
            url: ':socialhost:/:session_prefix:_/widget/render/youtube?usegapi\u003d1',
            methods: ['scroll', 'openwindow'],
          },
          ytsubscribe: { url: 'https://www.youtube.com/subscribe_embed?usegapi\u003d1' },
          plus_circle: { params: { url: '' }, url: ':socialhost:/:session_prefix::se:_/widget/plus/circle?usegapi\u003d1' },
          plus_share: {
            params: { url: '' },
            url: ':socialhost:/:session_prefix::se:_/+1/sharebutton?plusShare\u003dtrue\u0026usegapi\u003d1',
          },
          rbr_s: { params: { url: '' }, url: ':socialhost:/:session_prefix::se:_/widget/render/recobarsimplescroller' },
          ':source:': '3p',
          playemm: { url: 'https://play.google.com/work/embedded/search?usegapi\u003d1\u0026usegapi\u003d1' },
          savetoandroidpay: { url: 'https://pay.google.com/gp/v/widget/save' },
          blogger: {
            params: { location: ['search', 'hash'] },
            url: ':socialhost:/:session_prefix:_/widget/render/blogger?usegapi\u003d1',
            methods: ['scroll', 'openwindow'],
          },
          evwidget: { params: { url: '' }, url: ':socialhost:/:session_prefix:_/events/widget?usegapi\u003d1' },
          partnersbadge: { url: 'https://www.gstatic.com/partners/badge/templates/badge.html?usegapi\u003d1' },
          dataconnector: { url: 'https://dataconnector.corp.google.com/:session_prefix:ui/widgetview?usegapi\u003d1' },
          surveyoptin: { url: 'https://www.google.com/shopping/customerreviews/optin?usegapi\u003d1' },
          ':socialhost:': 'https://apis.google.com',
          shortlists: { url: '' },
          hangout: { url: 'https://talkgadget.google.com/:session_prefix:talkgadget/_/widget' },
          plus_followers: { params: { url: '' }, url: ':socialhost:/_/im/_/widget/render/plus/followers?usegapi\u003d1' },
          post: { params: { url: '' }, url: ':socialhost:/:session_prefix::im_prefix:_/widget/render/post?usegapi\u003d1' },
          ':gplus_url:': 'https://plus.google.com',
          signin: { params: { url: '' }, url: ':socialhost:/:session_prefix:_/widget/render/signin?usegapi\u003d1', methods: ['onauth'] },
          rbr_i: { params: { url: '' }, url: ':socialhost:/:session_prefix::se:_/widget/render/recobarinvitation' },
          share: { url: ':socialhost:/:session_prefix::im_prefix:_/widget/render/share?usegapi\u003d1' },
          plusone: { params: { count: '', size: '', url: '' }, url: ':socialhost:/:session_prefix::se:_/+1/fastbutton?usegapi\u003d1' },
          comments: {
            params: { location: ['search', 'hash'] },
            url: ':socialhost:/:session_prefix:_/widget/render/comments?usegapi\u003d1',
            methods: ['scroll', 'openwindow'],
          },
          ':im_socialhost:': 'https://plus.googleapis.com',
          backdrop: { url: 'https://clients3.google.com/cast/chromecast/home/widget/backdrop?usegapi\u003d1' },
          visibility: { params: { url: '' }, url: ':socialhost:/:session_prefix:_/widget/render/visibility?usegapi\u003d1' },
          autocomplete: { params: { url: '' }, url: ':socialhost:/:session_prefix:_/widget/render/autocomplete' },
          additnow: { url: 'https://apis.google.com/marketplace/button?usegapi\u003d1', methods: ['launchurl'] },
          ':signuphost:': 'https://plus.google.com',
          ratingbadge: { url: 'https://www.google.com/shopping/customerreviews/badge?usegapi\u003d1' },
          appcirclepicker: { url: ':socialhost:/:session_prefix:_/widget/render/appcirclepicker' },
          follow: { url: ':socialhost:/:session_prefix:_/widget/render/follow?usegapi\u003d1' },
          community: { url: ':ctx_socialhost:/:session_prefix::im_prefix:_/widget/render/community?usegapi\u003d1' },
          sharetoclassroom: { url: 'https://classroom.google.com/sharewidget?usegapi\u003d1' },
          ytshare: { params: { url: '' }, url: ':socialhost:/:session_prefix:_/widget/render/ytshare?usegapi\u003d1' },
          plus: { url: ':socialhost:/:session_prefix:_/widget/render/badge?usegapi\u003d1' },
          family_creation: { params: { url: '' }, url: 'https://families.google.com/webcreation?usegapi\u003d1\u0026usegapi\u003d1' },
          commentcount: { url: ':socialhost:/:session_prefix:_/widget/render/commentcount?usegapi\u003d1' },
          configurator: { url: ':socialhost:/:session_prefix:_/plusbuttonconfigurator?usegapi\u003d1' },
          zoomableimage: { url: 'https://ssl.gstatic.com/microscope/embed/' },
          appfinder: { url: 'https://workspace.google.com/:session_prefix:marketplace/appfinder?usegapi\u003d1' },
          savetowallet: { url: 'https://pay.google.com/gp/v/widget/save' },
          person: { url: ':socialhost:/:session_prefix:_/widget/render/person?usegapi\u003d1' },
          savetodrive: { url: 'https://drive.google.com/savetodrivebutton?usegapi\u003d1', methods: ['save'] },
          page: { url: ':socialhost:/:session_prefix:_/widget/render/page?usegapi\u003d1' },
          card: { url: ':socialhost:/:session_prefix:_/hovercard/card' },
        },
      },
      h: 'm;/_/scs/apps-static/_/js/k\u003doz.gapi.en.g8agzr_oroM.O/am\u003dAQ/d\u003d1/rs\u003dAGLTcCP6z3gW3iZ5SpDBmGgDQznnZEz5gQ/m\u003d__features__',
      u: 'https://apis.google.com/js/platform.js',
      hee: true,
      fp: 'e5f685b0f2087c66363bf458bab3c4029d2a33ae',
      dpo: false,
      le: ['ili'],
    },
    platform: [
      'additnow',
      'backdrop',
      'blogger',
      'comments',
      'commentcount',
      'community',
      'donation',
      'family_creation',
      'follow',
      'hangout',
      'health',
      'page',
      'partnersbadge',
      'person',
      'playemm',
      'playreview',
      'plus',
      'plusone',
      'post',
      'ratingbadge',
      'savetoandroidpay',
      'savetodrive',
      'savetowallet',
      'sharetoclassroom',
      'shortlists',
      'signin2',
      'surveyoptin',
      'visibility',
      'youtube',
      'ytsubscribe',
      'zoomableimage',
    ],
    fp: 'e5f685b0f2087c66363bf458bab3c4029d2a33ae',
    annotation: ['interactivepost', 'recobar', 'signin2', 'autocomplete', 'profile'],
    bimodal: ['signin', 'share'],
  },
});
