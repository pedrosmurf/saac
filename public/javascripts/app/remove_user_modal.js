jQuery('#removeModal').on('show.bs.modal', function (event) {
  var a = $(event.relatedTarget)
  var enrollment = a.data('enrollment')
  
  jQuery('#removeYesButton').attr('href','/admin/remove/'+enrollment)
})