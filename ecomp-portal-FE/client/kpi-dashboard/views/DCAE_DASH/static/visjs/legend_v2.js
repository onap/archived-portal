/*-
 * ================================================================================
 * eCOMP Portal
 * ================================================================================
 * Copyright (C) 2017 AT&T Intellectual Property
 * ================================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ================================================================================
 */
d3.select('#legend').style.visibility="visible";
 var svg1  = d3.select('#legend').append('svg');
 var g1 = svg1.append("g");
 svg1.attr('width', "100%")
    .attr('height', "100%");
 
  var circle1 = svg1.append("circle")
                .attr("cx", "10")
                .attr("cy", "10")
                .attr("r", "5")
                .attr('fill', greaterthan75_color)
                .attr('stroke', 'black')
                .attr('fill-opacity', 1.0);		
 
  
  var text4 = g1.append("text")
             .attr("x", "30")
			 .attr("y", "13")
            .attr("font-family", "sans-serif")
		  .attr("font-size", "10px");
		  text4.text(greaterthan75_text);
		  text4.attr("fill", "black");	

  var circle2 = svg1.append("circle")
                .attr("cx", "10")
                .attr("cy", "25")
                .attr("r", "5")
                .attr('fill', lessthan50_75_color)
                .attr('stroke', 'black')
                .attr('fill-opacity', 1.0);   

  var text5 = g1.append("text")
             .attr("x", "30")
			 .attr("y", "28")
            .attr("font-family", "sans-serif")
		  .attr("font-size", "10px");
		  text5.text(lessthan50_75_text);
		  text5.attr("fill", "black");
		  

    var circle3 = svg1.append("circle")
                .attr("cx", "10")
                .attr("cy", "40")
                .attr("r", "5")
                .attr('fill', lessthan50_color)
                .attr('stroke', 'black')
                .attr('fill-opacity', 1.0);  

  var text6 = g1.append("text")
             .attr("x", "30")
			 .attr("y", "43")
            .attr("font-family", "sans-serif")
		  .attr("font-size", "10px");
		  text6.text(lessthan50_text);
		  text6.attr("fill", "black"); 		
  		
 //var zoom = d3.behavior.zoom().on ("zoom", move);
 var zoom = d3.behavior.zoom()
  .on("zoom",function() {
    //alert(d3.event.scale); 
    g.attr("transform","translate("+d3.event.translate.join(",")+")scale("+d3.event.scale+")")
	if(d3.event.scale >= 4) { 
		d3.behavior.zoom()
		.scale(d3.event.scale);
	}
  });
