<%@ include file="/WEB-INF/template/include.jsp" %>

<% java.sql.Timestamp now = new java.sql.Timestamp(System.currentTimeMillis()); %>

<openmrs:htmlInclude file="/dwr/interface/DWROrderService.js" />
<openmrs:htmlInclude file="/dwr/engine.js" />
<openmrs:htmlInclude file="/dwr/util.js" />
<openmrs:htmlInclude file="/scripts/drugOrder.js" />

		<div id="regimenPortletCurrent">
			<table class="regimenCurrentTable">
				<thead>
					<tr class="regimenCurrentHeaderRow">
						<th style="nowrap: true;" class="regimenCurrentDrugOrderedHeader"> <spring:message code="Order.item.ordered" /> </th>
						<th class="regimenCurrentDrugDoseHeader"> <spring:message code="DrugOrder.dose"/>/<spring:message code="DrugOrder.units"/> </th>
						<th class="regimenCurrentDrugFrequencyHeader"> <spring:message code="DrugOrder.frequency"/> </th>
						<th class="regimenCurrentDrugDateStartHeader"> <spring:message code="general.dateStart"/> </th>
						<th class="regimenCurrentDrugScheduledStopDateHeader"> <spring:message code="DrugOrder.scheduledStopDate"/> </th>
						<th class="regimenCurrentDrugInstructionsHeader"> <spring:message code="general.instructions" /> </th>
						<c:if test="${model.currentRegimenMode != 'view'}">
							<th class="regimenCurrentEmptyHeader"> </th>
							<th class="regimenCurrentEmptyHeader"> </th>
						</c:if>
					</tr>
				</thead>
<c:choose>
	<c:when test="${not empty model.displayDrugSetIds}">
		<c:forTokens var="drugSetId" items="${model.displayDrugSetIds}" delims=",">
			<c:if test="${drugSetId == '*'}" >
				<tbody id="regimenTableCurrent_header___other__">
					<tr class="regimenCurrentHeaderOtherRow">
						<c:choose>
							<c:when test="${model.currentRegimenMode == 'view'}">
								<td colspan="6"><table><tr><td><spring:message code="DrugOrder.header.otherRegimens" /></td></tr></table></td>
							</c:when>
							<c:otherwise>
								<td colspan="8"><table><tr><td><spring:message code="DrugOrder.header.otherRegimens" /></td></tr></table></td>
							</c:otherwise>
						</c:choose>
					</tr>
				</tbody>
				<tbody id="regimenTableCurrent___other__">
					<c:if test="${not empty model.currentDrugOrderSets['*']}">
						<c:forEach items="${model.currentDrugOrderSets['*']}" var="drugOrder">
							<tr class="regimenCurrentDrugRow">
								<td class="regimenCurrentDrugEmptyData">&nbsp;&nbsp;&nbsp;&nbsp;
									<c:if test="${!empty drugOrder.drug}">
										<a class="patientRegimenDrugName" href="${pageContext.request.contextPath}/admin/orders/orderDrug.form?orderId=${drugOrder.orderId}">${drugOrder.drug.name}</a>
									</c:if>
									<c:if test="${empty drugOrder.drug}">
										<a class="patientRegimenDrugName" href="${pageContext.request.contextPath}/admin/orders/orderDrug.form?orderId=${drugOrder.orderId}">${drugOrder.concept.name.name}</a>
									</c:if>
								</td>
								<td class="regimenCurrentDrugDoseData">${drugOrder.dose} ${drugOrder.units}</td>
								<td class="regimenCurrentDrugFrequencyData">${drugOrder.frequency}</td>
								<td class="regimenCurrentDrugStartDateData"><openmrs:formatDate date="${drugOrder.startDate}" type="medium" /></td>
								<td class="regimenCurrentDrugAutoExpireDateData"><openmrs:formatDate date="${drugOrder.autoExpireDate}" type="medium" /></td>
								<td class="regimenCurrentDrugInstructionsData">${drugOrder.instructions}</td>
								<c:if test="${model.currentRegimenMode != 'view'}">
									<td class="regimenCurrentDrugDiscontinuedData">
										<input id="closebutton_${drugOrder.orderId}" type="button" value="<spring:message code="DrugOrder.discontinue" />" onClick="showHideDiv('close_${drugOrder.orderId}');showHideDiv('closebutton_${drugOrder.orderId}')" />
										<div id="close_${drugOrder.orderId}" style="display:none" class="dashedAndHighlighted">
											<form class="discontinuedDrugForm">
												<spring:message code="DrugOrder.discontinuedDate" />:
												<input type="text" id="close_${drugOrder.orderId}_date" size="10" value="" onFocus="showCalendar(this)" />
												&nbsp;&nbsp;&nbsp;&nbsp;
												<spring:message code="general.reason" />:
													<openmrs:fieldGen type="org.openmrs.DrugOrder.discontinuedReason" formFieldName="close_${drugOrder.orderId}_reason" val="" parameters="optionHeader=[blank]|globalProp=concept.reasonOrderStopped" />
												&nbsp;&nbsp;
												<input type="button" value="<spring:message code="DrugOrder.discontinue" />" onClick="handleDiscontinueDrugOrder('${drugOrder.orderId}', 'close_${drugOrder.orderId}_date', 'close_${drugOrder.orderId}_reason')" />
												<input type="button" value="<spring:message code="general.cancel" />" onClick="showHideDiv('close_${drugOrder.orderId}');showHideDiv('closebutton_${drugOrder.orderId}')" />
											</form>
										</div>
									</td>
									<td class="regimenCurrentDrugVoidData">
										<input id="voidbutton_${drugOrder.orderId}" type="button" value="<spring:message code="DrugOrder.void" />" onClick="showHideDiv('void_${drugOrder.orderId}');showHideDiv('voidbutton_${drugOrder.orderId}')" />
										<div id="void_${drugOrder.orderId}" style="display:none" class="dashedAndHighlighted">
											<form class="voidOrderDrugForm">
												<spring:message code="general.reason" />: 
													<select name="void_${drugOrder.orderId}_reason" id="void_${drugOrder.orderId}_reason">
														<option value=""></option>
														<option value="DrugOrder.void.reason.dateError"><spring:message code="DrugOrder.void.reason.dateError" /></option>
														<option value="DrugOrder.void.reason.error"><spring:message code="DrugOrder.void.reason.error" /></option>
														<option value="DrugOrder.void.reason.other"><spring:message code="DrugOrder.void.reason.other" /></option>
													</select>
												&nbsp;&nbsp;
												<input type="button" value="<spring:message code="DrugOrder.void" />" onClick="handleVoidCurrentDrugOrder('${drugOrder.orderId}', 'void_${drugOrder.orderId}_reason')" />
												<input type="button" value="<spring:message code="general.cancel" />" onClick="showHideDiv('void_${drugOrder.orderId}');showHideDiv('voidbutton_${drugOrder.orderId}')" />
											</form>
										</div>
									</td>
								</c:if>
							</tr>
						</c:forEach>
					</c:if>
					<c:if test="${empty model.currentDrugOrderSets['*']}">
						<tr class="regimenCurrentNoDrugOrdersRow">
							<c:choose>
								<c:when test="${model.currentRegimenMode == 'view'}">
									<td colspan="6" class="regimenCurrentViewModeData"><span class="noOrdersMessage">&nbsp;&nbsp;&nbsp;&nbsp;(<spring:message code="DrugOrder.list.noOrders" />)</span></td>
								</c:when>
								<c:otherwise>
									<td colspan="8" class="regimenCurrentOtherModeData"><span class="noOrdersMessage">&nbsp;&nbsp;&nbsp;&nbsp;(<spring:message code="DrugOrder.list.noOrders" />)</span></td>
								</c:otherwise>
							</c:choose>
						</tr>
					</c:if>
				</tbody>
			</c:if>
			<c:if test="${drugSetId != '*'}" >
				<tbody id="regimenTableCurrent_header_${fn:replace(drugSetId, " ", "_")}">
					<tr class="regimentTableCurrentHeaderRow">
						<c:choose>
							<c:when test="${model.currentRegimenMode == 'view'}">
								<td colspan="6" class="regimenCurrentViewModeData">
							</c:when>
							<c:otherwise>
								<td colspan="8" class="regimenCurrentOtherModeData">
							</c:otherwise>
						</c:choose>
							<table class="regimenCurrentConceptTable">
								<tr class="regimenCurrentConceptRow">
									<td class="regimenCurrentConceptData"><openmrs_tag:concept conceptId="${model.drugOrderHeaders[drugSetId].conceptId}" /></td>
									<c:if test="${(model.currentRegimenMode != 'view') && (not empty model.currentDrugOrderSets[drugSetId])}">
										<td class="regimenCurrentDrugDiscontinuedGroupData">
											<input id="closegpbutton_${fn:replace(drugSetId, " ", "_")}" type="button" value="<spring:message code="DrugOrder.discontinueGroup" />" onClick="showHideDiv('closegp_${fn:replace(drugSetId, " ", "_")}');showHideDiv('closegpbutton_${fn:replace(drugSetId, " ", "_")}')" />
											<div id="closegp_${fn:replace(drugSetId, " ", "_")}" style="display:none;" class="dashedAndHighlighted">
												<form class="regimenCurrentDrugDiscontinuedGroupForm">
													<spring:message code="DrugOrder.discontinuedDate" />:
													<input type="text" id="closegp_${fn:replace(drugSetId, " ", "_")}_date" size="10" value="" onFocus="showCalendar(this)" />
													&nbsp;&nbsp;&nbsp;&nbsp;
													<spring:message code="general.reason" />:
														<% if ( pageContext.getAttribute("drugSetId") != null ) pageContext.setAttribute("drugSetReplaced", ((String)pageContext.getAttribute("drugSetId")).replace(" ", "_")); %>
														<openmrs:fieldGen type="org.openmrs.DrugOrder.discontinuedReason" formFieldName="closegp_${drugSetReplaced}_reason" val="" parameters="optionHeader=[blank]|globalProp=concept.reasonOrderStopped" />
													&nbsp;&nbsp;
													<input type="button" value="<spring:message code="DrugOrder.discontinueGroup" />" onClick="handleDiscontinueDrugSet('${drugSetId}', 'closegp_${fn:replace(drugSetId, " ", "_")}_date', 'closegp_${fn:replace(drugSetId, " ", "_")}_reason')" />
													<input type="button" value="<spring:message code="general.cancel" />" onClick="showHideDiv('closegp_${fn:replace(drugSetId, " ", "_")}');showHideDiv('closegpbutton_${fn:replace(drugSetId, " ", "_")}');" />
												</form>
											</div>
										</td>
										<td class="regimenCurrentDrugVoidGroupData">
											<input id="voidgpbutton_${fn:replace(drugSetId, " ", "_")}" type="button" value="<spring:message code="DrugOrder.voidGroup" />" onClick="showHideDiv('voidgp_${fn:replace(drugSetId, " ", "_")}');showHideDiv('voidgpbutton_${fn:replace(drugSetId, " ", "_")}')" />
											<div id="voidgp_${fn:replace(drugSetId, " ", "_")}" style="display:none" class="dashedAndHighlighted" >
												<form class="regimenCurrentDrugVoidGroupForm">
													<spring:message code="general.reason" />:
														<select name="voidgp_${fn:replace(drugSetId, " ", "_")}_reason" id="voidgp_${fn:replace(drugSetId, " ", "_")}_reason">
															<option value=""></option>
															<option value="DrugOrder.void.reason.dateError"><spring:message code="DrugOrder.void.reason.dateError" /></option>
															<option value="DrugOrder.void.reason.error"><spring:message code="DrugOrder.void.reason.error" /></option>
															<option value="DrugOrder.void.reason.other"><spring:message code="DrugOrder.void.reason.other" /></option>
														</select>
													&nbsp;&nbsp;
													<input type="button" value="<spring:message code="DrugOrder.voidGroup" />" onClick="handleVoidCurrentDrugSet('${drugSetId}', 'voidgp_${fn:replace(drugSetId, " ", "_")}_reason')" />
													<input type="button" value="<spring:message code="general.cancel" />" onClick="showHideDiv('voidgp_${fn:replace(drugSetId, " ", "_")}');showHideDiv('voidgpbutton_${fn:replace(drugSetId, " ", "_")}')" />
												</form>
											</div>
										</td>
									</c:if>
								</tr>
							</table>
						</td>
					</tr>
				</tbody>
				<tbody id="regimenTableCurrent_${fn:replace(drugSetId, " ", "_")}">
					<c:if test="${not empty model.currentDrugOrderSets[drugSetId]}">
						<c:forEach items="${model.currentDrugOrderSets[drugSetId]}" var="drugOrder">
							<tr class="regimenCurrentDrugOrderRow">
								<td class="regimenCurrentDrugEmptyData">&nbsp;&nbsp;&nbsp;&nbsp;
									<c:if test="${!empty drugOrder.drug}">
										<a class="patientRegimenDrugName" href="${pageContext.request.contextPath}/admin/orders/orderDrug.form?orderId=${drugOrder.orderId}">${drugOrder.drug.name}</a>
									</c:if>
									<c:if test="${empty drugOrder.drug}">
										<a class="patientRegimenDrugName" href="${pageContext.request.contextPath}/admin/orders/orderDrug.form?orderId=${drugOrder.orderId}">${drugOrder.concept.name.name}</a>
									</c:if>
								</td>
								<td class="regimenCurrentDrugDoseData">${drugOrder.dose} ${drugOrder.units}</td>
								<td class="regimenCurrentDrugFrequencyData">${drugOrder.frequency}</td>
								<td class="regimenCurrentDrugStartDateData"><openmrs:formatDate date="${drugOrder.startDate}" type="medium" /></td>
								<td class="regimenCurrentDrugAutoExpireDateData"><openmrs:formatDate date="${drugOrder.autoExpireDate}" type="medium" /></td>
								<td  class="regimenCurrentDrugInstructionsData">${drugOrder.instructions}</td>
								<c:if test="${model.currentRegimenMode != 'view'}">
									<td class="regimenCurrentDrugDiscontinuedData">
										<input id="closebutton_${drugOrder.orderId}" type="button" value="<spring:message code="DrugOrder.discontinue" />" onClick="showHideDiv('close_${drugOrder.orderId}');showHideDiv('closebutton_${drugOrder.orderId}')" />
										<div id="close_${drugOrder.orderId}" style="display:none" class="dashedAndHighlighted" >
											<form class="discontinuedDrugForm">
												<spring:message code="DrugOrder.discontinuedDate" />:
												<input type="text" id="close_${drugOrder.orderId}_date" size="10" value="" onFocus="showCalendar(this)" />
												&nbsp;&nbsp;&nbsp;&nbsp;
												<spring:message code="general.reason" />:
													<openmrs:fieldGen type="org.openmrs.DrugOrder.discontinuedReason" formFieldName="close_${drugOrder.orderId}_reason" val="" parameters="optionHeader=[blank]|globalProp=concept.reasonOrderStopped" />
												&nbsp;&nbsp;
												<input type="button" value="<spring:message code="DrugOrder.discontinue" />" onClick="handleDiscontinueDrugOrder('${drugOrder.orderId}', 'close_${drugOrder.orderId}_date', 'close_${drugOrder.orderId}_reason')" />
												<input type="button" value="<spring:message code="general.cancel" />" onClick="showHideDiv('close_${drugOrder.orderId}');showHideDiv('closebutton_${drugOrder.orderId}')" />
											</form>
										</div>
									</td>
									<td class="regimenCurrentDrugVoidData">
										<input id="voidbutton_${drugOrder.orderId}" type="button" value="<spring:message code="DrugOrder.void" />" onClick="showHideDiv('void_${drugOrder.orderId}');showHideDiv('voidbutton_${drugOrder.orderId}')" />
										<div id="void_${drugOrder.orderId}" style="display:none" class="dashedAndHighlighted" >
											<form class="regimenCurrentDrugVoidForm">
												<spring:message code="general.reason" />: 
													<select name="void_${drugOrder.orderId}_reason" id="void_${drugOrder.orderId}_reason">
														<option value=""></option>
														<option value="DrugOrder.void.reason.dateError"><spring:message code="DrugOrder.void.reason.dateError" /></option>
														<option value="DrugOrder.void.reason.error"><spring:message code="DrugOrder.void.reason.error" /></option>
														<option value="DrugOrder.void.reason.other"><spring:message code="DrugOrder.void.reason.other" /></option>
													</select>
												&nbsp;&nbsp;
												<input type="button" value="<spring:message code="DrugOrder.void" />" onClick="handleVoidCurrentDrugOrder('${drugOrder.orderId}', 'void_${drugOrder.orderId}_reason')" />
												<input type="button" value="<spring:message code="general.cancel" />" onClick="showHideDiv('void_${drugOrder.orderId}');showHideDiv('voidbutton_${drugOrder.orderId}')" />
											</form>
										</div>
									</td>
								</c:if>
							</tr>
						</c:forEach>
					</c:if>
					<c:if test="${empty model.currentDrugOrderSets[drugSetId]}">
						<tr class="regimenCurrentNoDrugOrdersRow">
							<c:choose>
								<c:when test="${model.currentRegimenMode == 'view'}">
									<td colspan="6" class="regimenCurrentViewModeData"><span class="noOrdersMessage">&nbsp;&nbsp;&nbsp;&nbsp;(<spring:message code="DrugOrder.list.noOrders" />)</span></td>
								</c:when>
								<c:otherwise>
									<td colspan="8" class="regimenCurrentOtherModeData"><span class="noOrdersMessage">&nbsp;&nbsp;&nbsp;&nbsp;(<spring:message code="DrugOrder.list.noOrders" />)</span></td>
								</c:otherwise>
							</c:choose>
						</tr>
					</c:if>
				</tbody>
			</c:if>
		</c:forTokens>
	</c:when>
	<c:otherwise>
				<tbody id="regimenTableCurrent">
					<c:if test="${not empty model.currentDrugOrders}">
						<c:forEach items="${model.currentDrugOrders}" var="drugOrder">
							<tr class="regimenTableCurrentRow">
								<td class="regimenCurrentDrugNameData">
									<c:if test="${!empty drugOrder.drug}">
										<a class="patientRegimenDrugName" href="${pageContext.request.contextPath}/admin/orders/orderDrug.form?orderId=${drugOrder.orderId}">${drugOrder.drug.name}</a>
									</c:if>
									<c:if test="${empty drugOrder.drug}">
										<a class="patientRegimenDrugName" href="${pageContext.request.contextPath}/admin/orders/orderDrug.form?orderId=${drugOrder.orderId}">${drugOrder.concept.name.name}</a>
									</c:if>
								</td>
								<td class="regimenCurrentDrugDoseData">${drugOrder.dose} ${drugOrder.units}</td>
								<td class="regimenCurrentDrugFrequencyData">${drugOrder.frequency}</td>
								<td class="regimenCurrentDrugStartDateData"><openmrs:formatDate date="${drugOrder.startDate}" type="medium" /></td>
								<td class="regimenCurrentDrugAutoExpireDateData"><openmrs:formatDate date="${drugOrder.autoExpireDate}" type="medium" /></td>
								<td class="regimenCurrentDrugInstructionsData">${drugOrder.instructions}</td>
								<c:if test="${model.currentRegimenMode != 'view'}">
									<td class="regimenCurrentDrugDiscontinuedData">
										<input id="closebutton_${drugOrder.orderId}" type="button" value="<spring:message code="DrugOrder.discontinue" />" onClick="showHideDiv('close_${drugOrder.orderId}');showHideDiv('closebutton_${drugOrder.orderId}')" />
										<div id="close_${drugOrder.orderId}" style="display:none" class="dashedAndHighlighted" >
											<form class="discontinuedDrugForm">
												<spring:message code="DrugOrder.discontinuedDate" />:
												<input type="text" id="close_${drugOrder.orderId}_date" size="10" value="" onFocus="showCalendar(this)" />
												&nbsp;&nbsp;&nbsp;&nbsp;
												<spring:message code="general.reason" />:
													<openmrs:fieldGen type="org.openmrs.DrugOrder.discontinuedReason" formFieldName="close_${drugOrder.orderId}_reason" val="" parameters="optionHeader=[blank]|globalProp=concept.reasonOrderStopped" />
												&nbsp;&nbsp;
												<input type="button" value="<spring:message code="DrugOrder.discontinue" />" onClick="handleDiscontinueDrugOrder('${drugOrder.orderId}', 'close_${drugOrder.orderId}_date', 'close_${drugOrder.orderId}_reason')" />
												<input type="button" value="<spring:message code="general.cancel" />" onClick="showHideDiv('close_${drugOrder.orderId}');showHideDiv('closebutton_${drugOrder.orderId}')" />
											</form>
										</div>
									</td>
									<td class="regimenCurrentDrugVoidData">
										<input id="voidbutton_${drugOrder.orderId}" type="button" value="<spring:message code="DrugOrder.void" />" onClick="showHideDiv('void_${drugOrder.orderId}');showHideDiv('voidbutton_${drugOrder.orderId}')" />
										<div id="void_${drugOrder.orderId}" style="display:none" class="dashedAndHighlighted" >
											<form class="regimenCurrentDrugVoidForm">
												<spring:message code="general.reason" />: 
													<select name="void_${drugOrder.orderId}_reason" id="void_${drugOrder.orderId}_reason">
														<option value=""></option>
														<option value="DrugOrder.void.reason.dateError"><spring:message code="DrugOrder.void.reason.dateError" /></option>
														<option value="DrugOrder.void.reason.error"><spring:message code="DrugOrder.void.reason.error" /></option>
														<option value="DrugOrder.void.reason.other"><spring:message code="DrugOrder.void.reason.other" /></option>
													</select>
												&nbsp;&nbsp;
												<input type="button" value="<spring:message code="DrugOrder.void" />" onClick="handleVoidCurrentDrugOrder('${drugOrder.orderId}', 'void_${drugOrder.orderId}_reason')" />
												<input type="button" value="<spring:message code="general.cancel" />" onClick="showHideDiv('void_${drugOrder.orderId}');showHideDiv('voidbutton_${drugOrder.orderId}')" />
											</form>
										</div>
									</td>
								</c:if>
							</tr>
						</c:forEach>
					</c:if>
					<c:if test="${empty model.currentDrugOrders}">
						<tr class="regimenCurrentNoDrugOrdersRow">
							<c:choose>
								<c:when test="${model.currentRegimenMode == 'view'}">
									<td colspan="6" class="regimenCurrentViewModeData"><span class="noOrdersMessage">(<spring:message code="DrugOrder.list.noOrders" />)</span></td>
								</c:when>
								<c:otherwise>
									<td colspan="8" class="regimenCurrentOtherModeData"><span class="noOrdersMessage">(<spring:message code="DrugOrder.list.noOrders" />)</span></td>
								</c:otherwise>
							</c:choose>
						</tr>
					</c:if>
				</tbody>
	</c:otherwise>
</c:choose>
			</table>
			<script>
				setPatientId("${model.patientId}");
				setDisplayDrugSetIds("${model.displayDrugSetIds}");
				setRegimenMode("${model.currentRegimenMode}");
			</script>
		</div>

