/*
	Created by AhtUtils JSF Grid. http://ahtutils.sourceforge.net
	
	Based on 960 Grid System ~ Core CSS. Learn more ~ http://960.gs/
  	Licensed under GPL and MIT.
*/

/*
  Forces backgrounds to span full width, even if there is horizontal scrolling. Increase this if your layout is wider.
  Note: IE6 works fine without this fix.
*/

body {min-width: ${width}px;}

/* Container
----------------------------------------------------------------------------------------------------*/
.auContainer {
  margin-left: auto;
  margin-right: auto;
  width: ${width}px;
  padding-bottom: ${doublegutter}px;
}

.auOp6Container {
  margin-left: auto;
  margin-right: auto;
  width: ${op8Width}px;
  padding-bottom: ${doublegutter}px;
}
.auOp7Container {
  margin-left: auto;
  margin-right: auto;
  width: ${op8Width}px;
  padding-bottom: ${doublegutter}px;
}
.auOp8Container {
  margin-left: auto;
  margin-right: auto;
  width: ${op8Width}px;
  padding-bottom: ${doublegutter}px;
}
.auOp9Container {
  margin-left: auto;
  margin-right: auto;
  width: ${op8Width}px;
  padding-bottom: ${doublegutter}px;
}
.auOp10Container {
  margin-left: auto;
  margin-right: auto;
  width: ${op8Width}px;
  padding-bottom: ${doublegutter}px;
}

/* `Grid >> Global
----------------------------------------------------------------------------------------------------*/
[class*="auGrid"] {
  display: table;
  margin-left: ${gutter}px;
  margin-right: ${gutter}px;
}

/* Grid >> Children
----------------------------------------------------------------------------------------------------*/
.alpha {margin-left: 0;}
.omega {margin-right: 0;}

[class*="auGrid"] {margin-top: ${doublegutter}px;}

[class*="auGrid"] > div {margin-bottom: ${doublegutter}px;}
[class*="auGrid"] > div:last-child {margin-bottom: 0px;}

[class*="auGrid"] > form {margin-bottom: ${doublegutter}px;}
[class*="auGrid"] > form:last-child {margin-bottom: 0px;}

[class*="auGrid"] > form > div {margin-bottom: ${doublegutter}px;}
[class*="auGrid"] > form > div:last-child {margin-bottom: 0px;}

[class*="auGridSpace"] > div {margin-bottom: ${doublegutter}px;}
[class*="auGridSpace"] > div:last-child {margin-bottom: 0px;}

/* Grid >> 12 Columns
----------------------------------------------------------------------------------------------------*/
.auContainer .auGrid_1  {width: ${slot1}px;}
.auContainer .auGrid_2  {width: ${slot2}px;}
.auContainer .auGrid_3  {width: ${slot3}px;}
.auContainer .auGrid_4  {width: ${slot4}px;}
.auContainer .auGrid_5  {width: ${slot5}px;}
.auContainer .auGrid_6  {width: ${slot6}px;}
.auContainer .auGrid_7  {width: ${slot7}px;}
.auContainer .auGrid_8  {width: ${slot8}px;}
.auContainer .auGrid_9  {width: ${slot9}px;}
.auContainer .auGrid_10 {width: ${slot10}px;}
.auContainer .auGrid_11 {width: ${slot11}px;}
.auContainer .auGrid_12 {width: ${slot12}px;}

.jeeslDialog_1  {max-width: ${slot1}px; min-width: ${slot1}px;}
.jeeslDialog_2  {max-width: ${slot2}px; min-width: ${slot2}px;}
.jeeslDialog_3  {max-width: ${slot3}px; min-width: ${slot3}px;}
.jeeslDialog_4  {max-width: ${slot4}px; min-width: ${slot4}px;}
.jeeslDialog_5  {max-width: ${slot5}px; min-width: ${slot5}px;}
.jeeslDialog_6  {max-width: ${slot6}px; min-width: ${slot6}px;}
.jeeslDialog_7  {max-width: ${slot7}px; min-width: ${slot7}px;}
.jeeslDialog_8  {max-width: ${slot8}px; min-width: ${slot8}px;}
.jeeslDialog_9  {max-width: ${slot9}px; min-width: ${slot9}px;}
.jeeslDialog_10 {max-width: ${slot10}px; min-width: ${slot10}px;}
.jeeslDialog_11 {max-width: ${slot11}px; min-width: ${slot11}px;}
.jeeslDialog_12 {max-width: ${slot12}px; min-width: ${slot12}px;}

/* Grid >> Pull & Push
----------------------------------------------------------------------------------------------------*/
.push_1, .pull_1,
.push_2, .pull_2,
.push_3, .pull_3,
.push_4, .pull_4,
.push_5, .pull_5,
.push_6, .pull_6,
.push_7, .pull_7,
.push_8, .pull_8,
.push_9, .pull_9,
.push_10, .pull_10,
.push_11, .pull_11,
.push_12, .pull_12,
.push_13, .pull_13,
.push_14, .pull_14,
.push_15, .pull_15 {
  position: relative;
}

/* `Prefix Extra Space >> 12 Columns
----------------------------------------------------------------------------------------------------*/
.auContainer .prefix_1  {padding-left: 80px;}
.auContainer .prefix_2  {padding-left: 160px;}
.auContainer .prefix_3  {padding-left: 240px;}
.auContainer .prefix_4  {padding-left: 320px;}
.auContainer .prefix_5  {padding-left: 400px;}
.auContainer .prefix_6  {padding-left: 480px;}
.auContainer .prefix_7  {padding-left: 560px;}
.auContainer .prefix_8  {padding-left: 640px;}
.auContainer .prefix_9  {padding-left: 720px;}
.auContainer .prefix_10 {padding-left: 800px;}
.auContainer .prefix_11 {padding-left: 880px;}

/* `Suffix Extra Space >> 12 Columns
----------------------------------------------------------------------------------------------------*/
.auContainer .suffix_1  {padding-right: 80px;}
.auContainer .suffix_2  {padding-right: 160px;}
.auContainer .suffix_3  {padding-right: 240px;}
.auContainer .suffix_4  {padding-right: 320px;}
.auContainer .suffix_5  {padding-right: 400px;}
.auContainer .suffix_6  {padding-right: 480px;}
.auContainer .suffix_7  {padding-right: 560px;}
.auContainer .suffix_8  {padding-right: 640px;}
.auContainer .suffix_9  {padding-right: 720px;}
.auContainer .suffix_10 {padding-right: 800px;}
.auContainer .suffix_11 {padding-right: 880px;}

/* `Push Space >> 12 Columns
----------------------------------------------------------------------------------------------------*/
.auContainer .push_1  {left: 80px;}
.auContainer .push_2  {left: 160px;}
.auContainer .push_3  {left: 240px;}
.auContainer .push_4  {left: 320px;}
.auContainer .push_5  {left: 400px;}
.auContainer .push_6  {left: 480px;}
.auContainer .push_7  {left: 560px;}
.auContainer .push_8  {left: 640px;}
.auContainer .push_9  {left: 720px;}
.auContainer .push_10 {left: 800px;}
.auContainer .push_11 {left: 880px;}

/* `Pull Space >> 12 Columns
----------------------------------------------------------------------------------------------------*/
.auContainer .pull_1  {left: -80px;}
.auContainer .pull_2  {left: -160px;}
.auContainer .pull_3  {left: -240px;}
.auContainer .pull_4  {left: -320px;}
.auContainer .pull_5  {left: -400px;}
.auContainer .pull_6  {left: -480px;}
.auContainer .pull_7  {left: -560px;}
.auContainer .pull_8  {left: -640px;}
.auContainer .pull_9  {left: -720px;}
.auContainer .pull_10 {left: -800px;}
.auContainer .pull_11 {left: -880px;}

/* PrimeFaces hacks
----------------------------------------------------------------------------------------------------*/
.auFieldSetTop {margin-top: ${doublegutter}px;}
.auManualTop {margin-top: ${doublegutter}px;}
.auNoTop {margin-top: -${doublegutter}px;}
.auManualBottom {margin-bottom: ${doublegutter}px;}

/* `Clear Floated Elements
----------------------------------------------------------------------------------------------------*/
/* http://sonspring.com/journal/clearing-floats */
.clear {
  clear: both;
  display: block;
  overflow: hidden;
  visibility: hidden;
  width: 0;
  height: 0;
}

/* http://www.yuiblog.com/blog/2010/09/27/clearfix-reloaded-overflowhidden-demystified */

.clearfix:before,
.clearfix:after,
.auContainer:before,
.auContainer:after {
  content: '.';
  display: block;
  overflow: hidden;
  visibility: hidden;
  font-size: 0;
  line-height: 0;
  width: 0;
  height: 0;
}

.clearfix:after, .auContainer:after {clear: both;}

/*
  The following zoom:1 rule is specifically for IE6 + IE7.
  Move to separate stylesheet if invalid CSS is a problem.
*/
.clearfix, .auContainer, .container_16 {zoom: 1;}


/* `JeeslBlock >> Global
----------------------------------------------------------------------------------------------------*/
[class*="jeeslBlock"] {display: table;float: left;margin-left: -5px;margin-right: 0px;}

/* JeeslBlock >> Children
----------------------------------------------------------------------------------------------------*/
[class*="jeeslBlock"] > div:first-child { margin-left: 5px; margin-right: -34px; padding-inline-start: 0px; padding-right: 7px; margin-top: 1px; margin-bottom: -10px;}
[class*="jeeslBlock"] > div:last-child { margin-left: 5px; margin-right: -32px; padding-left: 32px; padding-right: 25px; margin-top:1px; margin-bottom: -10px;}

[class*="jeeslBlock"] {margin-top: 5px;}

[class*="jeeslBlock"] {margin-top: 0px;}
[class*="jeeslBlock"] > div:first-child {margin-bottom: -10px;}
[class*="jeeslBlock"] > div {margin-bottom: 10px;}

[class*="jeeslBlock"] > form {margin-bottom: ${doublegutter}px;}
[class*="jeeslBlock"] > form:last-child {margin-bottom: -5px;}

[class*="jeeslBlock"] > form > div {margin-bottom: ${doublegutter}px;}
[class*="jeeslBlock"] > form > div:last-child {margin-bottom: -5px;}

[class*="jeeslBlockSpace"] > div {margin-bottom: ${doublegutter}px;}
[class*="jeeslBlockSpace"] > div:last-child {margin-bottom: -5px;}

/* JeeslBlock >> 12 Columns
----------------------------------------------------------------------------------------------------*/
.auContainer .jeeslBlock_1  {width: ${blockslot1}px;}
.auContainer .jeeslBlock_2  {width: ${blockslot2}px;}
.auContainer .jeeslBlock_3  {width: ${blockslot3}px;}
.auContainer .jeeslBlock_4  {width: ${blockslot4}px;}
.auContainer .jeeslBlock_5  {width: ${blockslot5}px;}
.auContainer .jeeslBlock_6  {width: ${blockslot6}px;}
.auContainer .jeeslBlock_7  {width: ${blockslot7}px;}
.auContainer .jeeslBlock_8  {width: ${blockslot8}px;}
.auContainer .jeeslBlock_9  {width: ${blockslot9}px;}
.auContainer .jeeslBlock_10 {width: ${blockslot10}px;}
.auContainer .jeeslBlock_11 {width: ${blockslot11}px;}
.auContainer .jeeslBlock_12 {width: ${blockslot12}px;}