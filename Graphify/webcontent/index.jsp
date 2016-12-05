<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>

<%String baseUrl = getServletContext().getInitParameter("BaseUrl");%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">
    <title>GRAPHIFY</title>
    <link href="resources/css/bootstrap.min.css" rel="stylesheet">
    <link href="resources/css/animate.min.css" rel="stylesheet">
    <link href="resources/css/font-awesome.min.css" rel="stylesheet">
    <link href="resources/css/lightbox.css" rel="stylesheet">
    <link href="resources/css/main.css" rel="stylesheet">
    <link id="css-preset" href="resources/css/presets/preset1.css" rel="stylesheet">
    <link href="resources/css/responsive.css" rel="stylesheet">

    <!--[if lt IE 9]>
    <script src="resources/js/html5shiv.js"></script>
    <script src="resources/js/respond.min.js"></script>
    <![endif]-->

    <link href='http://fonts.googleapis.com/css?family=Open+Sans:300,400,600,700' rel='stylesheet' type='text/css'>
    <link rel="shortcut icon" href="resources/images/favicon.ico">

    <!-- For Popup Windows -->
    <link rel="stylesheet" href="https://code.jquery.com/mobile/1.4.5/jquery.mobile-1.4.5.min.css">
    <script src="https://code.jquery.com/jquery-1.11.3.min.js"></script>
    <script src="https://code.jquery.com/mobile/1.4.5/jquery.mobile-1.4.5.min.js"></script>
    <!-- End Popup Windows -->
    <script src="resources/js/dropzone.js"></script>
    <script src="resources/js/dropzone-amd-module.js"></script>
    <script type="text/javascript">

        var isZip = function (name) {
            return name.match(/zip$/i)
        };

        $(document).ready(function () {
            var file = $('[name="file"]');
            var imgContainer = $('#imgContainer');

            $('#btnUpload').on('click', function () {
                var filename = $.trim(file.val());

                if (!(isZip(filename))) {
                    alert('Please browse a ZIP file to upload ...');
                    return;
                }

                $.ajax({
                    url: '<%=baseUrl%>api/validate',
                    type: "POST",
                    data: new FormData(document.getElementById("fileForm")),
                    enctype: 'multipart/form-data',
                    processData: false,
                    contentType: false
                }).done(function (data) {
                    //imgContainer.html('');
                    var result = "Server returned with Message : " + data.message + " .";

                    /*$('#ReturnMessage').text(result);*/
                    //imgContainer.append(result);
                    if(data.isValid == true){
                        document.getElementById("btnUpload").disabled = true;
                        document.getElementById("btnConvert").disabled = false;

                        $('#btnConvert').on('click', function () {
                            $.ajax({
                                url: '<%=baseUrl%>api/conadd',
                                type: "POST",
                                data: new FormData(document.getElementById("fileForm")),
                                enctype: 'multipart/form-data',
                                processData: false,
                                contentType: false
                            }).done(function (data) {


                            }).fail(function (jqXHR, textStatus) {
                                //alert(jqXHR.responseText);
                                alert('Convert failed ...');
                            });
                        });

                    } else {
                        alert('Database not validated');
                    }

                }).fail(function (jqXHR, textStatus) {
                    //alert(jqXHR.responseText);
                    alert('File upload failed ...'+ '<%=baseUrl%>api/validate');
                });

            });

            $('#btnClear').on('click', function () {
                imgContainer.html('');
                file.val('');
            });
        });

    </script>
</head><!--/head-->

<body>

<!--.preloader-->
<div class="preloader"><i class="fa fa-circle-o-notch fa-spin"></i></div>
<!--/.preloader-->

<header id="home">
    <div id="home-slider" class="carousel slide carousel-fade" data-ride="carousel">
        <div class="carousel-inner">
            <div class="item active" style="background-image: url(resources/images/slider/1.jpg)">
                <div class="caption">
                    <h1 class="animated fadeInLeftBig">Welcome to <span>Graphify</span></h1>
                    <p class="animated fadeInRightBig">We create <span>solutions </span></p>
                    <a data-scroll class="btn btn-start animated fadeInUpBig" href="#services">Start now</a>
                </div>
            </div>
            <div class="item" style="background-image: url(resources/images/slider/2.jpg)">
                <div class="caption">
                    <h1 class="animated fadeInLeftBig">Say Hello to <span>Graphify</span></h1>
                    <p class="animated fadeInRightBig">We create solutions</p>
                    <a data-scroll class="btn btn-start animated fadeInUpBig" href="#services">Start now</a>
                </div>
            </div>
            <div class="item" style="background-image: url(resources/images/slider/3.jpg)">
                <div class="caption">
                    <h1 class="animated fadeInLeftBig">We are <span>Creative</span></h1>
                    <p class="animated fadeInRightBig">We create tool to convert Boring Tabular DB to Attractive Graph
                        DB</p>
                    <a data-scroll class="btn btn-start animated fadeInUpBig" href="#services">Start now</a>
                </div>
            </div>
        </div>
        <a class="left-control" href="#home-slider" data-slide="prev"><i class="fa fa-angle-left"></i></a>
        <a class="right-control" href="#home-slider" data-slide="next"><i class="fa fa-angle-right"></i></a>

        <a id="tohash" href="#services"><i class="fa fa-angle-down"></i></a>

    </div><!--/#home-slider-->
    <div class="main-nav">
        <div class="container">
            <div class="navbar-header">

                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                </button>
                <a class="navbar-brand" href="index.jsp">
                    <h1><img class="img-responsive" src="resources/images/logo.png" alt="logo" height="50" width="50">
                    </h1>
                </a>
            </div>
            <div class="collapse navbar-collapse">
                <ul class="nav navbar-nav navbar-right">
                    <li class="scroll active"><a href="#home">Home</a></li>
                    <li class="scroll"><a href="#services">Graphify Tool</a></li>
                    <li class="scroll"><a href="#about-us">About Us</a></li>

                    <li class="scroll"><a href="#team">Team</a></li>

                    <li class="scroll"><a href="#contact">Contact</a></li>
                </ul>
            </div>
        </div>
    </div><!--/#main-nav-->
</header><!--/#home-->
<section id="services">
    <div class="container">
        <div class="heading wow fadeInUp" data-wow-duration="1000ms" data-wow-delay="300ms">
            <div class="row">
                <div class="text-center col-sm-8 col-sm-offset-2">
                    <h2>Our Services</h2>
                    <p>We convert Normal tabular DB to Graph DB</p>
                </div>
            </div>
        </div>
        <div class="text-center our-services">
            <div class="row">
                <div class="col-sm-4 wow fadeInDown" data-wow-duration="1000ms" data-wow-delay="300ms">
                    <div class="service-icon">
                        <i class="fa fa-flask"></i>
                    </div>
                    <div class="service-info">
                        <h3><a href="http://google.com">Create Your DB</a></h3>
                        <p>Build you own GraphDB along with the vizulization of the same.</p>
                    </div>
                </div>
                <div class="col-sm-4 wow fadeInDown" data-wow-duration="1000ms" data-wow-delay="450ms">
                    <div class="service-icon">
                        <i class="fa fa-umbrella"></i>
                    </div>
                    <div class="service-info">
                        <h3><a href="#myPopup" data-rel="popup">Convert Your DB</a></h3>
                        <p>Upload your DB here and get it converted to IBM Graph DB</p>

                        <div data-role="popup" id="myPopup" class="ui-content" style="min-width:400px;">
                            <form id="fileForm" method="get" action="">
                                <div>
                                    <h3>Database Information</h3>
                                    <label for="host" class="ui-hidden-accessible">Host:</label>
                                    <input type="Text" name="host" id="host" placeholder="XXX.XXX.XXX.XXX" required>

                                    <label for="port" class="ui-hidden-accessible">Port:</label>
                                    <input type="text" name="port" id="port" placeholder="3306" required>

                                    <label for="db" class="ui-hidden-accessible">Database Name:</label>
                                    <input type="text" name="db" id="db" placeholder="Database Name" required>

                                    <label for="user" class="ui-hidden-accessible">User:</label>
                                    <input type="text" name="user" id="user" placeholder="User" required>

                                    <label for="password" class="ui-hidden-accessible">Database Password:</label>
                                    <input type="Password" name="password" id="password" placeholder="Password"
                                           required>

                                    <div id="dropzone">
                                        <input type="file" class="dropzone needsclick dz-clickable" id="demo-upload"
                                               name="file">
                                        <div class="dz-message needsclick">
                                            Drop database files here or click to upload.<br>
                                            <span class="note needsclick">(Please do <strong>not</strong> upload any other type of file)</span>
                                        </div>
                                    </div>
                                    <a id="ReturnMessage" />
                                    <button id="btnUpload" data-inline="true">Validate</button>
                                    <button id="btnConvert" data-inline="true" disabled>Convert</button>
                                    <div id="imgContainer"></div>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
                <div class="col-sm-4 wow fadeInDown" data-wow-duration="1000ms" data-wow-delay="550ms">
                    <div class="service-icon">
                        <i class="fa fa-cloud"></i>
                    </div>
                    <div class="service-info">
                        <h3>Learn more about GraphDB</h3>
                        <p>Want to learn more check out some links to learn more about GraphDB</p>
                    </div>
                </div>
            </div>
        </div>
    </div>
</section><!--/#services-->

<section id="about-us" class="parallax">
    <div class="container">
        <div class="row">
            <div class="col-sm-6">
                <div class="about-info wow fadeInUp" data-wow-duration="1000ms" data-wow-delay="300ms">
                    <h2>About us</h2>
                    <p>We group of four are highly enthusiastic coders and creaters, looking forward to develop
                        something that will ease people to easily migrate the Tabular DataBase to the latest Graph
                        DB </p>
                    <p>We would be soon extending it over the cloud.</p>
                </div>
            </div>
            <div class="col-sm-6">
                <div class="our-skills wow fadeInDown" data-wow-duration="1000ms" data-wow-delay="300ms">
                    <div class="single-skill wow fadeInDown" data-wow-duration="1000ms" data-wow-delay="300ms">
                        <p class="lead">Efficient Conversion of databases</p>
                        <div class="progress">
                            <div class="progress-bar progress-bar-primary six-sec-ease-in-out" role="progressbar"
                                 aria-valuetransitiongoal="95">95%
                            </div>
                        </div>
                    </div>
                    <div class="single-skill wow fadeInDown" data-wow-duration="1000ms" data-wow-delay="400ms">
                        <p class="lead">Useful Graph database</p>
                        <div class="progress">
                            <div class="progress-bar progress-bar-primary six-sec-ease-in-out" role="progressbar"
                                 aria-valuetransitiongoal="75">75%
                            </div>
                        </div>
                    </div>
                    <div class="single-skill wow fadeInDown" data-wow-duration="1000ms" data-wow-delay="500ms">
                        <p class="lead">People found it more efficient</p>
                        <div class="progress">
                            <div class="progress-bar progress-bar-primary six-sec-ease-in-out" role="progressbar"
                                 aria-valuetransitiongoal="60">60%
                            </div>
                        </div>
                    </div>
                    <div class="single-skill wow fadeInDown" data-wow-duration="1000ms" data-wow-delay="600ms">
                        <p class="lead">It's moreFun</p>
                        <div class="progress">
                            <div class="progress-bar progress-bar-primary six-sec-ease-in-out" role="progressbar"
                                 aria-valuetransitiongoal="85">85%
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</section><!--/#about-us-->
<section id="team">
    <div class="container">
        <div class="row">
            <div class="heading text-center col-sm-8 col-sm-offset-2 wow fadeInUp" data-wow-duration="1200ms"
                 data-wow-delay="300ms">
                <h2>The Team</h2>
                <p>Main Contributors</p>
            </div>
        </div>
        <div class="team-members">
            <div class="row">
                <div class="col-sm-3">
                    <div class="team-member wow flipInY" data-wow-duration="1000ms" data-wow-delay="300ms">
                        <div class="member-image">
                            <img class="img-responsive" src="resources/images/team/n.jpg" alt="" style="width:304px;height:228px;">
                        </div>
                        <div class="member-info">
                            <h3>Nakshatra</h3>
                            <h4>Designer &amp; Code</h4>
                            <p>Goood</p>
                        </div>
                        <div class="social-icons">
                            <ul>
                                <li><a class="facebook" href="#"><i class="fa fa-facebook"></i></a></li>
                                <li><a class="twitter" href="#"><i class="fa fa-twitter"></i></a></li>
                                <li><a class="linkedin" href="#"><i class="fa fa-linkedin"></i></a></li>

                            </ul>
                        </div>
                    </div>
                </div>
                <div class="col-sm-3">
                    <div class="team-member wow flipInY" data-wow-duration="1000ms" data-wow-delay="500ms">
                        <div class="member-image">
                            <img class="img-responsive" src="resources/images/team/sa.jpg" alt="" style="width:304px;height:228px;">
                        </div>
                        <div class="member-info">
                            <h3>Sohrab Ali</h3>
                            <h4>UI/UX Designer</h4>
                            <p>Todu Banda</p>
                        </div>
                        <div class="social-icons">
                            <ul>
                                <li><a class="facebook" href="#"><i class="fa fa-facebook"></i></a></li>
                                <li><a class="twitter" href="#"><i class="fa fa-twitter"></i></a></li>
                                <li><a class="linkedin" href="#"><i class="fa fa-linkedin"></i></a></li>

                            </ul>
                        </div>
                    </div>
                </div>
                <div class="col-sm-3">
                    <div class="team-member wow flipInY" data-wow-duration="1000ms" data-wow-delay="800ms">
                        <div class="member-image">
                            <img class="img-responsive" src="resources/images/team/sk.jpg" alt="" style="width:304px;height:228px;">
                        </div>
                        <div class="member-info">
                            <h3>Swathi Koduri</h3>
                            <h4>Developer and UI</h4>
                            <p>The Singing DIVAloper</p>
                        </div>
                        <div class="social-icons">
                            <ul>
                                <li><a class="facebook" href="#"><i class="fa fa-facebook"></i></a></li>
                                <li><a class="twitter" href="#"><i class="fa fa-twitter"></i></a></li>
                                <li><a class="linkedin" href="#"><i class="fa fa-linkedin"></i></a></li>

                            </ul>
                        </div>
                    </div>
                </div>
                <div class="col-sm-3">
                    <div class="team-member wow flipInY" data-wow-duration="1000ms" data-wow-delay="1100ms">
                        <div class="member-image">
                            <img class="img-responsive" src="resources/images/team/sv.jpg" alt="" style="width:304px;height:228px;">
                        </div>
                        <div class="member-info">
                            <h3>Sushant</h3>
                            <h4>Backend</h4>
                            <p>0 Risk Analysis</p>
                        </div>
                        <div class="social-icons">
                            <ul>
                                <li><a class="facebook" href="#"><i class="fa fa-facebook"></i></a></li>
                                <li><a class="twitter" href="#"><i class="fa fa-twitter"></i></a></li>
                                <li><a class="linkedin" href="#"><i class="fa fa-linkedin"></i></a></li>

                            </ul>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</section><!--/#team-->

<section id="contact">

    <div id="contact-us" class="parallax">
        <div class="container">
            <div class="row">
                <div class="heading text-center col-sm-8 col-sm-offset-2 wow fadeInUp" data-wow-duration="1000ms"
                     data-wow-delay="300ms">
                    <h2>Contact Us</h2>
                    <p>Team - 12 CMPE 272</p>
                </div>
            </div>
            <div class="contact-form wow fadeIn" data-wow-duration="1000ms" data-wow-delay="600ms">
                <div class="row">
                    <div class="col-sm-6">
                        <form id="main-contact-form" name="contact-form" method="post" action="#">
                            <div class="row  wow fadeInUp" data-wow-duration="1000ms" data-wow-delay="300ms">
                                <div class="col-sm-6">
                                    <div class="form-group">
                                        <input type="text" name="name" class="form-control" placeholder="Name"
                                               required="required">
                                    </div>
                                </div>
                                <div class="col-sm-6">
                                    <div class="form-group">
                                        <input type="email" name="email" class="form-control"
                                               placeholder="Email Address" required="required">
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <input type="text" name="subject" class="form-control" placeholder="Subject"
                                       required="required">
                            </div>
                            <div class="form-group">
                                <textarea name="message" id="message" class="form-control" rows="4"
                                          placeholder="Enter your message" required="required"></textarea>
                            </div>
                            <div class="form-group">
                                <button type="submit" class="btn-submit">Send Now</button>
                            </div>
                        </form>
                    </div>
                    <div class="col-sm-6">
                        <div class="contact-info wow fadeInUp" data-wow-duration="1000ms" data-wow-delay="300ms">
                            <p>We would love to hear the feedback</p>
                            <ul class="address">
                                <li><i class="fa fa-map-marker"></i> <span> Address:</span> SAN JOSE STATE UNIVERSITY
                                </li>
                                <li><i class="fa fa-phone"></i> <span> Phone:</span> (707)-867-5867</li>
                                <li><i class="fa fa-envelope"></i> <span> Email:</span><a
                                        href="mailto:someone@yoursite.com"> we@gaphify.com </a></li>
                                <li><h3><i class="fa fa-globe"></i><span> Website:</span> <a
                                        href="#">www.graphify.com</a></h3></li>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</section><!--/#contact-->
<footer id="footer">
    <div class="footer-top wow fadeInUp" data-wow-duration="1000ms" data-wow-delay="300ms">
        <div class="container text-center">
            <div class="footer-logo">
                <a href="index.jsp"><img class="img-responsive" src="resources/images/logo.png" alt="" height="60"
                                         width="60"></a>
            </div>
            <div class="social-icons">
                <ul>
                    <li><a class="envelope" href="#"><i class="fa fa-envelope"></i></a></li>
                </ul>
            </div>
        </div>
    </div>
    <div class="footer-bottom">
        <div class="container">
            <div class="row">
                <div class="col-sm-6">
                    <p>&copy; 2016, Team - 12 (CMPE-272)</p>
                </div>
            </div>
        </div>
    </div>
</footer>

<script type="text/javascript" src="resources/js/jquery.js"></script>
<script type="text/javascript" src="resources/js/bootstrap.min.js"></script>
<script type="text/javascript" src="http://maps.google.com/maps/api/js?sensor=true"></script>
<script type="text/javascript" src="resources/js/jquery.inview.min.js"></script>
<script type="text/javascript" src="resources/js/wow.min.js"></script>
<script type="text/javascript" src="resources/js/mousescroll.js"></script>
<script type="text/javascript" src="resources/js/smoothscroll.js"></script>
<script type="text/javascript" src="resources/js/jquery.countTo.js"></script>
<script type="text/javascript" src="resources/js/lightbox.min.js"></script>
<script type="text/javascript" src="resources/js/main.js"></script>

</body>
</html>