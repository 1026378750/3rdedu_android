function getQueryString(name) {
  var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
  var r = window.location.search.substr(1).match(reg);
  if (r != null) {
    return decodeURI(r[2]);
  } else {
    return '';
  }
}

function getzf(num) {
  if (parseInt(num) < 10) {
    num = '0' + num;
  }
  return num;
}

function getMyDate(str) {
  var oDate = new Date(str),
    oYear = oDate.getFullYear(),
    oMonth = oDate.getMonth() + 1,
    oDay = oDate.getDate(),
    oTime = oYear + '-' + getzf(oMonth) + '-' + getzf(oDay);//最后拼接时间
  return oTime;
}

var common = {
  /**
   * 接收一个Long型，除以10万得到浮点类型
   * @param long 数据库中的值
   * @param fixed 保留几位小数（默认2位）
   * @returns {string}
   */
  toFloat: function (a, b) {
    return (a / 10000).toFixed(b ? b : 2);
  }
}

var token, linkUrl,teacherId;
  // Token = getQueryString("token") === "" ? "eyJ0eXBlIjoiSldUIiwiYWxnIjoiSFMyNTYifQ.eyJsb2dpbk1vYmlsZSI6IjEzMDAwMDAwMDA3IiwibG9naW5Db2RlIjoiNTYiLCJsb2dpblRpbWUiOjE1MjU5MzI5MTM1ODIsImlzcyI6InN6eS4zcmQiLCJhdWQiOiIwOThmNmJjZDQ2MjFkMzczY2FkZTRlODMyNjI3YjRmNiJ9.4VrPw5qJCX8lPY26kYjloR7fFIEBi1ER_TWx09K6sAE" : getQueryString("token");
// var teacherId = getQueryString("teacherId") === "" ? 25 : getQueryString("teacherId");

var userDifferentiation = 1;

if (getQueryString("linkUrl") === "" || localStorage.linkUrl == "") {
  localStorage.userDifferentiation = userDifferentiation;
  if (getQueryString("userDifferentiation") == 1 || localStorage.userDifferentiation == 1) { //老师端
    linkUrl = "http://10.8.1.204/3rdedu/";
    localStorage.linkUrl = linkUrl;
  } else if (getQueryString("userDifferentiation") == 2 || localStorage.userDifferentiation == 2) { //家长端
    linkUrl = "http://10.8.1.204/3rdeduBase/";
    localStorage.linkUrl = linkUrl;
  }
} else {
  linkUrl = getQueryString("linkUrl");
  userDifferentiation = getQueryString("userDifferentiation");
  localStorage.linkUrl = linkUrl;
  localStorage.userDifferentiation = userDifferentiation;
  if(localStorage.userDifferentiation == 1){

  } else {
    teacherId = getQueryString("teacherId");
    localStorage.teacherId = teacherId;
  }
}

if(getQueryString("linkUrl") == "" || localStorage.linkUrl == "") {
  if(localStorage.userDifferentiation == 1) {
    localStorage.token = "eyJ0eXBlIjoiSldUIiwiYWxnIjoiSFMyNTYifQ.eyJsb2dpbk1vYmlsZSI6IjEzMDAwMDAwMDA3IiwibG9naW5Db2RlIjoiNTYiLCJsb2dpblRpbWUiOjE1MjU5MzI5MTM1ODIsImlzcyI6InN6eS4zcmQiLCJhdWQiOiIwOThmNmJjZDQ2MjFkMzczY2FkZTRlODMyNjI3YjRmNiJ9.4VrPw5qJCX8lPY26kYjloR7fFIEBi1ER_TWx09K6sAE";
  } else {
    localStorage.token = "";
  }
} else {
  token = getQueryString("token");
  localStorage.token = token;
}

//ajax封装start
var key = CryptoJS.enc.Utf8.parse("MrQz9Nq8mNOpcFNI");
var iv = CryptoJS.enc.Utf8.parse("dMitHORyqbeYVE0o");

function encrypt(word) {
  var encrypted = CryptoJS.AES.encrypt(word, key, {iv: iv, mode: CryptoJS.mode.CBC, padding: CryptoJS.pad.Pkcs7});
  return JSON.stringify({sign: encrypted.toString()})
}

function Decrypt(word) {
  var word = word.replace(/(\r\n)|(\n)/g, "");
  var decryptedData = CryptoJS.AES.decrypt(word, key, {iv: iv, mode: CryptoJS.mode.CBC, padding: CryptoJS.pad.Pkcs7});
  var decryptedStr = decryptedData.toString(CryptoJS.enc.Utf8);
  return decryptedStr;
}

var ajaxUtil = function () {
  var ajax = function (param, callBack) {
    var ajaxData = param.data.json == undefined ? param.data : encrypt(param.data.json);
    param.url = localStorage.linkUrl + param.url;
    var ajaxParam = {
      type: param.type,
      url: param.url,
      cache: false,
      data: ajaxData,
      beforeSend: function (request) {
        request.setRequestHeader("authorization", localStorage.token),
          request.setRequestHeader("Client-Info", "ShengZheYuan;1.0;pc;1.0;pc;win8"),
          request.setRequestHeader("Content-Type", "application/json;charset=utf-8")
      },
      success: function (res) {
        if (typeof(res) == "string" || typeof(res) == "String") {
          res = JSON.parse(res)
        }
        var redata = JSON.parse(Decrypt(res.sign));
        if (redata.error_code == "222222") {
          callBack(redata);
        }
      },
      error: function (xhr, textStatus) {
        console.log(xhr);
        // console.log("xhr:"+xhr)
        // console.log("textStatus:"+textStatus)
        // if(xhr.responseText["error"]!=undefined){
        // $.growl.error({title: "错误！", message: xhr.responseText, duration: 5999});
        // }
      },
      complete: function () {
        ////console.log('结束')
      }
    };
    if (param.dataType != "") {
      ajaxParam.dataType = param.dataType;
    }

    if (param.contentType != "" && param.async != "") {
      ajaxParam.contentType = param.contentType;
    }

    $.ajax(ajaxParam);
  };
  return {
    ajaxGet: function (params, callBack) {
      params.type = "GET";
      params.dataType = "";
      params.contentType = "";
      ajax(params, callBack);
    },

    ajaxPost: function (params, callBack) {
      params.type = "POST";
      params.dataType = "";
      ajax(params, callBack);
    },

    ajaxJson: function (params, callBack) {
      params.type = "POST";
      params.dataType = "json";
      params.contentType = "application/json;charset=utf-8";
      ajax(params, callBack);
    },

    ajaxJsonp: function (params, callBack) {
      params.type = "Get";
      params.dataType = "jsonp";
      params.contentType = "";
      params.async = false,
        ajax(params, callBack);
    },
  }
}();
//ajax封装end