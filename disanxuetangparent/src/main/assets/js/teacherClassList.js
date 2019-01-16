function teacherClassList() {
  var len = $(".transformWrap").children().length;
  $(".transformWrap").width(100 * len + "vw");

  function winScroll() {
    var _scrollTop = $(window).scrollTop();
    if ($(".oneToOneHeader").hasClass("active")) {
      var len = $(".oneToOneClassWrap .oneToOneClassItem").length;
      var _height = $(".oneToOneClassWrap").height();
      var number = len % 15;
      if (len == 0) {
        return
      } else if (number == 0 && _scrollTop > _height * 0.5) {
        _pageOneToOne.pageNum++
        _initOneToOne();
      } else if(number != 0) {
        $(".oneToOneClassWrap .teacherBottomAll").removeClass("displayNone");
      }
    } else if ($(".lineClassHeader").hasClass("active")) {
      var len = $(".lineClassWrap .lineClassItem").length;
      var _height = $(".lineClassWrap").height();
      var number = len % 15;
      if (len == 0) {
        return
      } else if (number == 0 && _scrollTop > _height * 0.5) {
        _pageLineClass.pageNum++;
        _initLineClass();
      } else if(number != 0) {
        $(".lineClassWrap .teacherBottomAll").removeClass("displayNone");
      }
    } else if ($(".onLineClassHeader").hasClass("active")) {
      var len = $(".onLineClassWrap .onLineClassItem").length;
      var _height = $(".onLineClassWrap").height();
      var number = len % 15;
      if (len == 0) {
        return
      } else if (number == 0 && _scrollTop > _height * 0.5) {
        _pageOnLine.pageNum++;
        _initOnLineClass();
      } else if(number != 0) {
        $(".onLineClassWrap .teacherBottomAll").removeClass("displayNone");
      }
    }
  }

  function _initOneToOne() {
    ajaxUtil.ajaxPost({
      url: "teaInfo/teacherCourseArray",
      data: {json: JSON.stringify(_pageOneToOne)}
    }, function (msg) {
      var data = msg.data;
      var len = $(".oneToOneClassWrap ul>li.oneToOneClassItem").length;
      if (data.list.length <= 0 && len == 0) {
        $(".courseWrap .oneToOneHeader").addClass("displayNone");
        $(".courseWrap .oneToOneHeader").attr("data", "");
        $(".courseWrap .lineClassHeader").attr("data", "1");
        $(".courseWrap .onLineClassHeader").attr("data", "2");
      }
      data.list.map(function (v, index) {
        var studentClass = "", teacherClass = "", campusClass = "";
        if (v.studentPrice !== null) {
          studentClass = "active";
        }
        if (v.teacherPrice !== null) {
          teacherClass = "active";
        }
        if (v.campusPrice !== null) {
          campusClass = "active";
        }
        var payBtnClass = "", payBtnText = "";
        if (v.isQuotaFull) {
          payBtnClass = "";
          payBtnText = "名额已满";
        } else {
          payBtnClass = "active";
          payBtnText = "立即购买";
        }
        var price, disCountPrice, _cls;
        if (v.minPrice == 0) {
          disCountPrice = "免费";
          _cls = "displayNone";
          price = "";
        } else {
          if (v.minPrice - v.minDiscountPrice == 0) {
            disCountPrice = "￥" + common.toFloat(v.minPrice);
            price = "";
            _cls = "displayNone";
          } else {
            disCountPrice = "￥" + common.toFloat(v.minDiscountPrice);
            price = "￥" + common.toFloat(v.minPrice);
            _cls = ""
          }
        }
        var _html = "<li class='oneToOneClassItem' data='"+ JSON.stringify(v) +"'><div class='oneToOneClassBg'>" + (index + 1) + "</div><div class='oneToOneClassDetails'><div><div class='floatRight'><span class='oneToOneClassCousePrice'>" + disCountPrice + "</span>起 <div class='onToOneOriginalPrice " + _cls + "'>" + price + "</div></div><div class='clearfix oneToOneClassCourseWrap'><span class='oneToOneClassCourseSuject'>" + v.gradeName + v.subjectName + "-</span><span class='oneToOneClassName'>" + v.courseName + "</span></div></div><div class='oneToOneClassExplain'>" + v.remark + "</div><div class='clearfix'><div class='floatRight payBtn " + payBtnClass + "'>" + payBtnText + "</div><ul class='oneToOneClassMode'><li class='floatLeft " + studentClass + "'>学生上门</li><li class='floatLeft " + teacherClass + "'>老师上门</li><li class='floatLeft " + campusClass + "'>校区上课</li></ul></div></div></li>"
        $(".allClassWrap .oneToOneClassWrap>ul").append(_html);
      });
      $(".oneToOneClassItem").map(function (v,index) {
        $(this).click(function () {
          var objectString = $(this).attr("data");
           window:android.setMessage(objectString);
        })
      })
      if ($(".oneToOneClassWrap ul>li.oneToOneClassItem").length < 15) {
        $(".oneToOneClassWrap .teacherBottomAll").removeClass("displayNone");
      }
    });
  }

  function _initLineClass() {
    ajaxUtil.ajaxPost({
      url: "teaInfo/teacherCourseArray",
      data: {json: JSON.stringify(_pageLineClass)}
    }, function (msg) {
      var data = msg.data;
      var len = $(".lineClassWrap ul>li.lineClassItem").length;
      if (data.list.length <= 0 && len == 0) {
        $(".courseWrap .lineClassHeader").addClass("displayNone");
        $(".courseWrap .oneToOneHeader").attr("data", "1");
        $(".courseWrap .lineClassHeader").attr("data", "");
        $(".courseWrap .onLineClassHeader").attr("data", "2");
      }
      data.list.map(function (v, index) {
        var payBtnClass = "", payBtnText = "";
        if (v.isQuotaFull) {
          payBtnClass = "";
          payBtnText = "名额已满";
        } else {
          payBtnClass = "active";
          payBtnText = "立即购买";
        }
        var price, disCountPrice, _cls;
        if (v.price == 0) {
          disCountPrice = "免费";
          _cls = "displayNone";
          price = "";
        } else {
          if (v.price - v.discountPrice == 0) {
            disCountPrice = "￥" + common.toFloat(v.price);
            price = "";
            _cls = "displayNone";
          } else {
            disCountPrice = "￥" + common.toFloat(v.discountPrice);
            price = "￥" + common.toFloat(v.price);
            _cls = ""
          }
        }
        var _html = "<li class='lineClassItem' data='" + JSON.stringify(v) + "'><div class='lineClassBg' style='background-image: url(" + v.photoUrl + ")'></div><div class='lineClassDetails'><div><div class='floatRight lineClassDetailsRight'><span class='oneToOneClassPeople'>" + v.salesVolume + "</span><span class='oneToOneClassMaxPeople'>/" + v.maxUser + "人</span></div><div class='clearfix lineClassCourseWrap'><span class='lineClassCourseSuject'>" + v.gradeName + v.subjectName + "-</span><span class='lineClassName'>" + v.courseName + "</span></div></div><div class='lineClassExplain'>" + v.remark + "</div><div class='clearfix'><div class='floatRight payBtn " + payBtnClass + "'>" + payBtnText + "</div><div class='lineClassMode'><span class='lineClassPrice'>" + disCountPrice + "</span><span class='lineClassPriceOrigin " + _cls + "'>" + price + "</span><span class='lineClassTime'>" + v.classTime + "次</span></div></div></div></li>"
        $(".allClassWrap .lineClassWrap>ul").append(_html);
      });
      $(".lineClassItem").map(function (v,index) {
        $(this).click(function () {
          var objectString = $(this).attr("data");
          window:android.setLineClassMessage(objectString);
        })
      })
    });
  }

  function _initOnLineClass() {
    ajaxUtil.ajaxPost({url: "teaInfo/teacherCourseArray", data: {json: JSON.stringify(_pageOnLine)}}, function (msg) {
      var data = msg.data;
      if (data.list.length <= 0) {
        $(".courseWrap .onLineClassHeader").addClass("displayNone");
      }
      data.list.map(function (v, index) {
        var payBtnClass = "", payBtnText = "";
        if (v.isQuotaFull) {
          payBtnClass = "";
          payBtnText = "名额已满";
        } else {
          payBtnClass = "active";
          payBtnText = "立即购买";
        }
        var price, disCountPrice, _cls;
        if (v.price == 0) {
          disCountPrice = "免费";
          _cls = "displayNone";
          price = "";
        } else {
          if (v.price - v.discountPrice == 0) {
            disCountPrice = "￥" + common.toFloat(v.price);
            price = "";
            _cls = "displayNone";
          } else {
            disCountPrice = "￥" + common.toFloat(v.discountPrice);
            price = "￥" + common.toFloat(v.price);
            _cls = ""
          }
        }
        var _html = "<li class='onLineClassItem' data='"+ JSON.stringify(v) +"'><div class='onLineClassBg'><div class='onLineClassImg' style='background-image: url(" + v.photoUrl + ")'></div><div class='onLineClassText'>" + v.directTypeName + "</div></div><div class='onLineClassDetails'><div><div class='clearfix onLineClassCourseWrap'><span class='onLineClassCourseSuject'>" + v.gradeName + v.subjectName + "-</span><span class='onLineClassName'>" + v.courseName + "</span></div></div><div class='onLineClassExplain'>" + v.remark + "</div><div class='onLineClassDetailsRight'><span class='onLineClassPeople'>" + v.salesVolume + "</span><span class='onLineClassMaxPeople'>/" + v.maxUser + "人</span></div><div class='clearfix'><div class='floatRight payBtn " + payBtnClass + "'>" + payBtnText + "</div><div class='lineClassMode'><span class='lineClassPrice onLineClassPrice'>" + disCountPrice + "</span><span class='lineClassPriceOrigin " + _cls + "'>" + price + "</span><span class='lineClassTime'>" + v.classTime + "次</span></div></div></div></li>"
        $(".allClassWrap .onLineClassWrap>ul").append(_html);
      });
      $(".onLineClassItem").map(function (v,index) {
        $(this).click(function () {
          var objectString = $(this).attr("data");
            window:android.setOnLineMessage(objectString);
        })
      })
    });
  }

  if (localStorage.userDifferentiation == 1) { //老师端
    //老师信息初始化------start
    ajaxUtil.ajaxGet({url: "teaInfo/teaHomePage", data: {}}, function (msg) {
      var data = msg.data;
      $(".teacherDetailWrap .teacherImg").css({"background-image": "url(" + data.photoUrl + ")"});
      $(".teacherDetailWrap .teacherBg").css({"background-image": "url(" + data.photoUrl + ")"});
      $(".teacherDetailWrap .teacherName").html(data.teacherName);
      if (data.identity == 1) {
        $(".teacherDetailWrap .teacherRdedu").show();
      } else {
        $(".teacherDetailWrap .teacherRdedu").hide();
      }
      var _cls = "";
      _cls = data.sex == 0 ? "girl" : "boy";
      $(".teacherDetailWrap .teacherSex").addClass(_cls);
      $(".teacherDetailWrap .teacherSubject").html(data.subjectName);
      $(".teacherDetailWrap .teacherAge").html(data.teachingAge + "年教龄");
      $(".teacherDetailWrap .studentNumber .teacherTitle").html(data.studentNum);
      $(".teacherDetailWrap .orderNumber .teacherTitle").html(data.orderNum);
      $(".teacherDetailWrap .teachingTime .teacherTitle").html(data.timeLong);
      $(".teacherBriefIntroduction p").html(data.personalResume);
      $(".teachingSchool p").html(data.geaduateSchool + "&nbsp;&nbsp;" + data.edu + "&nbsp;&nbsp;" + data.profession);
      $(".teacherDetailWrap .oneToOneHeader span.number").html("(" + data.courseOneNum + ")");
      $(".teacherDetailWrap .lineClassHeader span.number").html("(" + data.courseSquadNum + ")");
      $(".teacherDetailWrap .onLineClassHeader span.number").html("(" + data.courseDirectNum + ")");
      data.teachingExperience.map(function (v, i) {
        var _html = '<li class="experienceItem"><div><span class="experienceItemCircular"></span>' + getMyDate(v.startTime) + '至' + getMyDate(v.endTime) + '</div><div class="experienceItemContent"><div class="teacherSchoolName">' + v.school + '</div><div class="teacherSchoolExperience">' + v.remark + '</div></div> </li>'
        $(".teachingExperience .teachingExperienceWrap").append(_html);
      })
    });
    //老师信息初始化------end
    // 线下1对1----start
    var _pageOneToOne = {pageSize: 15, pageNum: 1, courseType: 1};
    _initOneToOne();
    // 线下1对1----end
    // 线下班课----start
    var _pageLineClass = {pageSize: 15, pageNum: 1, courseType: 3};
    _initLineClass();
    // 线下班课----end
    // 在线直播----start
    var _pageOnLine = {pageSize: 15, pageNum: 1, courseType: 2};
    _initOnLineClass();
    // 在线直播----end
    $(window).scroll(function () {
      winScroll()
    })
  } else { //家长端
    var teacherPost = {teacherId: localStorage.teacherId};
    //老师信息初始化------start
    ajaxUtil.ajaxPost({url: "teaInfo/teaHomePage", data: {json: JSON.stringify(teacherPost)}}, function (msg) {
      var data = msg.data;
      $(".teacherDetailWrap .teacherImg").css({"background-image": "url(" + data.photoUrl + ")"});
      $(".teacherDetailWrap .teacherBg").css({"background-image": "url(" + data.photoUrl + ")"});
      $(".teacherDetailWrap .teacherName").html(data.teacherName);
      if (data.identity == 1) {
        $(".teacherDetailWrap .teacherRdedu").show();
      } else {
        $(".teacherDetailWrap .teacherRdedu").hide();
      }
      var _cls = "";
      _cls = data.sex == 0 ? "girl" : "boy";
      $(".teacherDetailWrap .teacherSex").addClass(_cls);
      $(".teacherDetailWrap .teacherSubject").html(data.subjectName);
      $(".teacherDetailWrap .teacherAge").html(data.teachingAge + "年教龄");
      $(".teacherDetailWrap .studentNumber .teacherTitle").html(data.studentNum);
      $(".teacherDetailWrap .orderNumber .teacherTitle").html(data.orderNum);
      $(".teacherDetailWrap .teachingTime .teacherTitle").html(data.timeLong);
      $(".teacherBriefIntroduction p").html(data.personalResume);
      $(".teachingSchool p").html(data.geaduateSchool + "&nbsp;&nbsp;" + data.edu + "&nbsp;&nbsp;" + data.profession);
      $(".teacherDetailWrap .oneToOneHeader span.number").html("(" + data.courseOneNum + ")");
      $(".teacherDetailWrap .lineClassHeader span.number").html("(" + data.courseSquadNum + ")");
      $(".teacherDetailWrap .onLineClassHeader span.number").html("(" + data.courseDirectNum + ")");
      data.teachingExperience.map(function (v, i) {
        var _html = '<li class="experienceItem"><div><span class="experienceItemCircular"></span>' + getMyDate(v.startTime) + '至' + getMyDate(v.endTime) + '</div><div class="experienceItemContent"><div class="teacherSchoolName">' + v.school + '</div><div class="teacherSchoolExperience">' + v.remark + '</div></div> </li>'
        $(".teachingExperience .teachingExperienceWrap").append(_html);
      })
    });
    //老师信息初始化------end
    // 线下1对1----start
    var _pageOneToOne = {pageSize: 15, pageNum: 1, courseType: 1, teacherId: localStorage.teacherId};
    _initOneToOne();
    // 线下1对1----end
    // 线下班课----start
    var _pageLineClass = {pageSize: 15, pageNum: 1, courseType: 3, teacherId: localStorage.teacherId};
    _initLineClass();
    // 线下班课----end
    // 在线直播----start
    var _pageOnLine = {pageSize: 15, pageNum: 1, courseType: 2, teacherId: localStorage.teacherId};
    _initOnLineClass();
    // 在线直播----end
    $(window).scroll(function () {
      winScroll();
    })
  }

  $(".courseWrap").on("click", "li", function () {
    $(this).addClass("active").siblings().removeClass("active");
    $(this).find("div").addClass("animated bounceInRight");
    $(this).siblings().find("div").removeClass("animated bounceInRight");
    var index = $(this).attr("data");
    var _text = $(this).find(".courseName").text();
    var _onLineHeight = $(".transformWrap .onLineClassWrap>ul").height();
    var _oneToOneHeight = $(".transformWrap .oneToOneClassWrap>ul").height();
    var _lineClassHeight = $(".transformWrap .lineClassWrap>ul").height();
    if (_text == "线下1对1") {
      $(".allClassWrap").height(_oneToOneHeight + 41);
    } else if (_text == "线下班课") {
      var len = $(".lineClassWrap .lineClassItem").length;
      if (len <= 15) {
        $(".lineClassWrap .teacherBottomAll").removeClass("displayNone");
      }
      $(".allClassWrap").height(_lineClassHeight + 41);
    } else if (_text == "在线课程") {
      var len = $(".onLineClassWrap .onLineClassItem").length;
      if (len <= 15) {
        $(".onLineClassWrap .teacherBottomAll").removeClass("displayNone");
      }
      $(".allClassWrap").height(_onLineHeight + 41);
    }
    $(".transformWrap").css({"transform": "translateX(-" + (index - 1) * 100 + "vw)"});
  })
}