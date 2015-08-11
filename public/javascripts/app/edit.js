var validEvent = true
var validDescription = true
var validParticipation = true
var validInstitution = true
var validPeriod = true
var validWorkload = true


function validateForm(){
	if(validEvent && validDescription && validParticipation && validInstitution && validPeriod && validWorkload){
		jQuery("#submit").removeAttr('disabled')
	}else{
		jQuery("#submit").attr('disabled','disabled')
	}
}


function showAlert(field){
	jQuery("#"+field+"Div").addClass("has-error").addClass("has-feedback")
	jQuery("#x"+field).removeClass("hide").addClass("show")
	jQuery("#"+field+"ErrorMessage").removeClass("hide").addClass("show")
}

function removeAlert(field){
	jQuery("#"+field+"Div").removeClass("has-error").removeClass("has-feedback")
	jQuery("#x"+field).removeClass("show").addClass("hide")
	jQuery("#"+field+"ErrorMessage").removeClass("show").addClass("hide")
}

jQuery("#event").focusout(function(){
	var value = jQuery("#event").val()
	if(value == ""){
		validEvent = false
		showAlert("event")
	}else{
		validEvent = true
		removeAlert("event")
	}
	validateForm()
})
jQuery("#description").focusout(function(){
	var value = jQuery("#description").val()
	if(value == ""){
		validDescription = false
		showAlert("description")
	}else{
		validDescription = true
		removeAlert("description")
	}
	validateForm()
})
jQuery("#participation").focusout(function(){
	var value = jQuery("#participation").val()
	if(value == ""){
		validParticipation = false
		showAlert("participation")
	}else{
		validParticipation = true
		removeAlert("participation")
	}
	validateForm()
})
jQuery("#institution").focusout(function(){
	var value = jQuery("#institution").val()
	if(value == ""){
		validInstitution = false
		showAlert("institution")
	}else{
		validInstitution = true
		removeAlert("institution")
	}
	validateForm()
})
jQuery("#period").focusout(function(){
	var value = jQuery("#period").val()
	if(value == ""){
		validPeriod = false
		showAlert("period")
	}else{
		validPeriod = true
		removeAlert("period")
	}
	validateForm()
})

jQuery("#workload").focusout(function(){
	var workload = parseInt(jQuery("#workload").val())
	var maxWorkload = parseInt(jQuery("#maxWorkloadValue").val())
	
	if(isNaN(workload) || workload > maxWorkload){
		jQuery("#workloadDiv").addClass("has-error").addClass("has-feedback")
		jQuery("#xworkload").removeClass("hide").addClass("show")
		jQuery("#errorMessageWorkload").removeClass("hide").addClass("show")
		jQuery("#maxWorkload").html(maxWorkload)
		validWorkload = false
	}else{
		jQuery("#workloadDiv").removeClass("has-error").removeClass("has-feedback")
		jQuery("#xworkload").removeClass("show").addClass("hide")
		jQuery("#errorMessageWorkload").removeClass("show").addClass("hide")
		jQuery("#maxWorkload").html("")
		validWorkload = true
	}
	validateForm()
})

jQuery(function(){
	validateForm()
})
