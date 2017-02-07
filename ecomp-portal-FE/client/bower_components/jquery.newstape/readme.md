JQuery Newstape
===================
Simple Touch-enabled News Ticker Plugin

Newstape is a really small jQuery text scroller plugin which automatically & vertically scrolls through a list of news feeds with support for mouse wheel, mouse drag and touch swipe events.

[DEMO](http://www.jqueryscript.net/demo/Simple-Touch-enabled-News-Ticker-Plugin-Newstape/)

How to use it:
-------------
Load jQuery library and the jQuery newstape plugin in your html page.
``` html
<script src="//code.jquery.com/jquery-1.11.2.min.js"></script>
<script src="jquery.newstape.js"></script>
```
Load the optional jQuery mousewheel plugin for mouse wheel support.
``` html
<script src="jquery.mousewheel.min.js"></script>
```
Load the optional jquery.event.drag plugin for drag & swipe support.
``` html
<script src="jquery.event.drag.min.js"></script>
```
Create a basic news ticker as follow.
``` html
<div class="newstape">
  <div class="newstape-content">
    <div class="news-block">
      <h3>News 1</h3>
      <small>13.04.2015</small>
      <p class="text-justify"> Content 1 ... </p>
      <div class="text-right"> <a href="#">More</a> </div>
      <hr />
    </div>
    <div class="news-block">
      <h3>News 2</h3>
      <small>13.04.2015</small>
      <p class="text-justify"> Content 2 ... </p>
      <div class="text-right"> <a href="#">More</a> </div>
      <hr />
    </div>
    <div class="news-block">
      <h3>News 3</h3>
      <small>13.04.2015</small>
      <p class="text-justify"> Content 3 ... </p>
      <div class="text-right"> <a href="#">More</a> </div>
      <hr />
    </div>
  </div>
</div>
```
Add your own CSS styles to the news ticker.
``` css
.newstape {
  background-color: #3BB0D6;
  color: #fff;
  height: 400px;
  overflow: hidden;
}

.newstape-content {
  position: relative;
  padding: 15px;
}

.newstape-drag { cursor: ns-resize; }

.text-center { text-align: center; }

.text-right { text-align: right; }

.text-justify { text-align: justify; }
```
Call the plugin to start the news ticker.
``` javascript
$('.newstape').newstape();

```
Default settings.
``` javascript
$('.newstape').newstape({

// timer period
period: 30, 

// offset pixel count
offset: 1, 

// mousewheel scrolling
mousewheel: true, 

// mousewheel offset pixel count
mousewheelRate: 30, 

// dragging tape content
dragable: true

});
```
