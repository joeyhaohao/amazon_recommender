$('#rate-form').submit(function(e) {
    e.preventDefault();
    var form = $('#rate-form');
    $.ajax({
        url: form.attr('action'),
        data: form.serialize(),
        type: post,
        success: function(result) {
            //
        }, error: function(error) {
            //
        }
    })
})
