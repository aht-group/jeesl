/*
Created by AhtUtils Prototype. http://ahtutils.sourceforge.net
Please do not modify this file to avoid inconsistencies!

These parameters a defined by the custom maven configuration:
dark: ${colorDark}
medium: ${colorMedium}
light: ${colorLight}
*/


a { color: ${colorDark};}

.header ul li.active > a {
border-bottom: 2px solid ${colorDark};
color: ${colorDark};
}

.header li a:hover{
border-bottom: 2px solid ${colorDark};
color: ${colorDark};
}

.aupHeadline {background-color: ${colorDark};}


/* SIDEMENU */
.sidemenu {width: 100%;}
.sidemenu a {
    display: block;

    /* white-space: nowrap; */
    max-width: 180px;

    color:#FFFFFF;
    border-bottom:solid 1px #EDEDED;

    text-align:right;
}
.sidemenu a:hover{
 color: #FFF;
 background: ${colorMedium};
}

/*.sidemenu ul {
    margin: -10px 0 0 10px;
    padding: 0 3px 0 0;
}*/
.sidemenu > ul > li {
    list-style: none outside none;
}
.sidemenu > ul > li > a {
    background-image: linear-gradient(to left, ${colorMedium}, ${colorDark});
    padding-right:10px;
}
.sidemenu > ul > li > ul > li > a {
    background-image: linear-gradient(to left, ${colorLight}, ${colorMedium});
    padding-right:20px;
}
.sidemenu > ul > li > ul > li > ul > li > a {
    background-image: linear-gradient(to left, ${colorLight}, ${colorMedium});
    padding-right:30px;
}



/* BREADCRUMBS */
ul.aupHeadlineBreadcrumb {
  float:right;
  position: relative;
  right: -20px;
  top: -6px;
}
ul.aupHeadlineBreadcrumb,
.aupHeadlineBreadcrumb ul {
  list-style: none;
  padding: 0;
  margin: 0;
}
/* .aupHeadlineBreadcrumb li a , .aupHeadlineBreadcrumb li ul li a { */
.aupHeadlineBreadcrumb a {
  position: relative;

  /* display: inline-block; */
  display: block;
  white-space: nowrap;

  background-color: #DDDDDD;
  background-image: linear-gradient(to right, ${colorDark}, ${colorLight});
  color: #FFFFFF;
  padding: 7px 1em 6px 2em;
}

.aupHeadlineBreadcrumb a:hover {
    background: none repeat scroll 0 0 ${colorDark};
}
/* .aupHeadlineBreadcrumb a:after, .aupHeadlineBreadcrumb a:before */
.aupHeadlineBreadcrumb > li > label > a:after, .aupHeadlineBreadcrumb > li > label > a:before,
.aupHeadlineBreadcrumb > li > ul > li > a:after, .aupHeadlineBreadcrumb > li > ul > li > a:before {
    border-bottom: 1.0em solid rgba(0, 0, 0, 0);
    border-left: 1em solid;
    border-top: 1.0em solid rgba(0, 0, 0, 0);
    content: "";
    margin-top: -1.0em;
    position: absolute;
    right: -1em;
    top: 50%;
}
/* .aupHeadlineBreadcrumb a:after { */
.aupHeadlineBreadcrumb > li > label > a:after, .aupHeadlineBreadcrumb > li > ul > li > a:after {
    border-left-color: ${colorLight};
    z-index: 2;
}
/* .aupHeadlineBreadcrumb a:before { */
.aupHeadlineBreadcrumb > li > label > a:before, .aupHeadlineBreadcrumb > li > ul > li > a:before {
    border-left-color: ${colorLight};
    right: -1.1em;
    z-index: 1;
}
/* .aupHeadlineBreadcrumb a:hover:after { */
.aupHeadlineBreadcrumb > li > label > a:hover:after, .aupHeadlineBreadcrumb > li > ul > li > a:hover:after  {
    border-left-color: ${colorDark};
}
.aupHeadlineBreadcrumb > li:last-child a {
  /* Next line trims the last child */
  overflow: hidden;
}
.aupHeadlineBreadcrumb > li > label > a + span {
	display: none;
}
.aupHeadlineBreadcrumb > li > ul > li:first-child {
	display: none;
}

.aupHeadlineBreadcrumb .current, .aupHeadlineBreadcrumb .current:hover {
    background: none repeat scroll 0 0 rgba(0, 0, 0, 0);
    font-weight: bold;
}
.aupHeadlineBreadcrumb .current:after, .aupHeadlineBreadcrumb .current:before {
    content: normal;
}

.aupHeadlineBreadcrumb > li {
  position: relative;
  float: left;
  text-wrap:none;
}
.aupHeadlineBreadcrumb > li:hover ul {
  opacity: 1;
  visibility: visible;
  -webkit-transition-delay: 0s, 0s;
  -moz-transition-delay: 0s, 0s;
  -o-transition-delay: 0s, 0s;
  transition-delay: 0s, 0s;
}
.aupHeadlineBreadcrumb ul {
  text-align:right;
  right:0;

  opacity: 0;
  visibility: hidden;
  z-index: 1;
  position: absolute;
  -webkit-transition-property: opacity, visibility;
  -moz-transition-property: opacity, visibility;
  -o-transition-property: opacity, visibility;
  transition-property: opacity, visibility;
  -webkit-transition-duration: .2s, 0s;
  -moz-transition-duration: .2s, 0s;
  -o-transition-duration: .2s, 0s;
  transition-duration: .2s, 0s;
  -webkit-transition-delay: 0s, .2s;
  -moz-transition-delay: 0s, .2s;
  -o-transition-delay: 0s, .2s;
  transition-delay: 0s, .2s;
}

/* ------------------------------------------------ */

.aupStatusBar {
	height:25px;
	/* margin: 10px; */
	margin:-10px 10px 10px 10px;
	color:#FFFFFF;
	/* W3C for :  vertical gradient from dark to light blue (on the first 10px, remaining should be light blue) */
	background: linear-gradient(to bottom, ${colorDark} 0%,${colorMedium} 40%,${colorMedium} 100%);
}

/* Equivalent to CSS selector .aupHeadlineBreadcrumb */
.aupStatusBar > ul {
	clear:both;
	display:block;
	list-style: none;
	padding: 0;
	margin: 0;
}

.aupStatusBar > ul > li:hover {
	background: ${colorDark};
}

.aupStatusBar > ul > li > ul > li:hover {
    background: none repeat scroll 0 0 ${colorDark};
}

.aupStatusBar > ul > li img {
	height: 12px;
}

.aupStatusBar > ul > li.aupStatusBarLeft,
.aupStatusBar > ul > li.aupStatusBarRight {
	position: relative;
	/* This margin pushes the next li far enough to squeeze in a CSS separator */
	margin-right:5px;
	/* push the ul down to offset the position due to the parent ul's clear:both */
	top:-6px;
}

.aupStatusBar > ul > li.aupStatusBarLeft {
	float:left;
}

.aupStatusBar > ul > li.aupStatusBarRight {
	float:right;
	padding: 5px 1em 6px 1.2em;
}

/* Separator for li. These must use different selectors or the results look weird */
.aupStatusBar > ul > li.aupStatusBarLeft:not(:first-child):before,
.aupStatusBar > ul > li.aupStatusBarRight:not(:last-child):after {
    content: "|";   
    position: absolute;
    /* The next line pushes the CSS separator just outside of this li but outside of the next li */
    left:-5px;
    /* The next line positions the CSS separator somewhere in the middle of the horizontal div */
    top:5px;
}

.aupStatusBar > ul > li > ul {
	list-style: none;
	padding: 0;
	margin: 0;

	text-align:right;
	right:0;

	opacity: 0;
	visibility: hidden;
	z-index: 1;
	position: absolute;
	-webkit-transition-property: opacity, visibility;
	-moz-transition-property: opacity, visibility;
	-o-transition-property: opacity, visibility;
	transition-property: opacity, visibility;
	-webkit-transition-duration: .2s, 0s;
	-moz-transition-duration: .2s, 0s;
	-o-transition-duration: .2s, 0s;
	transition-duration: .2s, 0s;
	-webkit-transition-delay: 0s, .2s;
	-moz-transition-delay: 0s, .2s;
	-o-transition-delay: 0s, .2s;
	transition-delay: 0s, .2s;
}

/* .aupStatusBar > ul > li:hover ul { */
.aupStatusBar input[type=checkbox] {display:none;}

/* Position the top of the ul at the bottom of its parent */
/*.aupStatusBar input[type=checkbox]:checked + ul { 
  top:25px;

  opacity: 1;
  visibility: visible;
  -webkit-transition-delay: 0s, 0s;
  -moz-transition-delay: 0s, 0s;
  -o-transition-delay: 0s, 0s;
  transition-delay: 0s, 0s;
}*/

.aupStatusBar > ul > li.aupStatusBarLeft > ul {
	text-align:left;
	/* The magic line to prevent the li from being hidden by the short width of its parent*/
	right:auto;
	/* Align left with parent */
	left:0;
}

/* Give the drop down li some body */
.aupStatusBar > ul > li, 
.aupStatusBar > ul > li > ul > li {
	padding: 7px 1em 6px 1.2em;
}

#aupStatusBarReports > li {
	width: 400px;
	white-space: normal;
}

.aupStatusBar > ul > li > ul > li {
	display: block;
	white-space: nowrap;
	background-color: ${colorMedium};
}

/* Reverse the padding horizontally for the left floating li */
.aupStatusBar > ul > li.aupStatusBarLeft,
.aupStatusBar > ul > li.aupStatusBarLeft > ul > li {
	padding: 7px 0.8em 6px 0.6em;
}

.aupStatusBar > ul > li:hover > ul {
	display:block;
	float:left;
	opacity:1;
	visibility:visible;
}

.aupStatusBar > ul > li > ul > li >  a {
	color: #FFF;
	display: block;
	margin: -3px -15px -3px -20px;
	padding: 3px 15px 3px 20px;
}

.jeeslSbFlatSelected > a {text-decoration: underline;}
.jeeslSbFlat > label > a {color: white;}

.aupStatusBarCalendar > .ui-inputfield{
	background-color: transparent;
	border: none;
	text-shadow: none;
	color: white;
	height: 8px;
	width: 55px;
}

.aupStatusBarDpFirst > .ui-inputfield {
	text-align: right;
}

.aupDateGraphic {
	background: white;
	margin: 0 5px;
}

.jeeslSchedule {
position: relative;
z-index: 0;
}

.alert .constTitle {
margin-left: 10px;
font-weight: bold;
vertical-align: sub;
}