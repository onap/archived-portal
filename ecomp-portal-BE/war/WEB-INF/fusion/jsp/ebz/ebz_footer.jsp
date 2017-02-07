<%--
  ================================================================================
  eCOMP Portal
  ================================================================================
  Copyright (C) 2017 AT&T Intellectual Property
  ================================================================================
  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at
  
       http://www.apache.org/licenses/LICENSE-2.0
  
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
  ================================================================================
  --%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
  <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<script src="static/ebz/js/footer.js"></script>
<script type="text/javascript" src="static/ebz/js/attHeaderSnippet.js"></script>
<link rel="stylesheet" type="text/css" href="static/ebz/footer_new.css" >
 


<div class="footerContainer" id="footerContainer">
	<div class="footerWrapper" id="footerWrapper">

			<div class="footerItem">
		
			</div>
			<div class="feedbackSection">
				<div class="dividerTop"></div>
				<div class="feedbackContent">
					<form id="feedbackForm" name="feedbackForm" action="javascript: void(0)"  method="post">
						<div class="feedbackInfo">Please tell us about your AT&amp;T Business Center experience. Your feedback is appreciated and will help us improve the site.</div>
						<div class="feedbackTextSection">
							<textarea id="feedbackText" class="feedbackText" name="feedbackText" maxlength="4000"></textarea>
							<button type="submit" class="feedbackSubmitButton" id="feedbackSubmitButton" name ="feedbackSubmitButton" >Submit</button>
						</div>
					</form>
				</div>
			</div>
			<div class="dividerBottom" id="feedbackResultDivider"></div>
			<div class="feedbackResultMsg">
				<div class='readFeedBackMessage' tabindex='-1' style="outline:0px; display:inline-block" aria-live="assertive"></div>
				<span class="icon-included-checkmark" id="feedbackMsgCheck"></span>
				<button id="feedbackOkButton" class="feedbackMsgOKButton">OK</button>
			</div>
			<div class="dividerBottom"></div>
			<div class="feedbackButtonDiv">
				<button type="button" class="feedbackButton" id="feedbackButton" name ="feedbackButton" >Feedback<span class="icon-chevron-down"></span></button>
			</div>
	
		<div class="attFooterInfo">
			<div class="footerLastSection">
			</div>
		</div>
	</div>
</div>
