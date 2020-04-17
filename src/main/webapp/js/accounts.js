function init() {}

$('#accountModal').on('show.bs.modal', function(event) {
    // Unbind handlers to avoid situations in which this button has more than one onclick handler
    $('#accountModalSubmit').unbind();

    let accountId = $(event.relatedTarget).data('account-id');
    let link = accountId
        // If accountId is valid, then we are in edit mode
        ? CONTEXT_PATH + '/account/edit/' + accountId
        // Otherwise we are in creation mode
        : CONTEXT_PATH + '/account/add';

    // Update modal's title depending on mode
    $('#accountModalLabel').html(accountId ? 'Edit Account <span class="small text-gray-500">#' + accountId + '</span>' : 'Add New Account');

    $.get(link, function(data) {
        $('#accountModalBody').html(data);
        $('#accountModalSubmit').click(function(event) {
            // Submit
            $.post(link, $('#accountModalForm').serialize(), function () {
                window.location.reload();
            });
        });
    });
});

$('#accountDeleteModal').on('show.bs.modal', function(event) {
    // Unbind handlers to avoid situations in which this button has more than one onclick handler
    const submitButton = $('#accountDeleteModalSubmit');
    submitButton.unbind();

    let accountId = $(event.relatedTarget).data('account-id');

    $('#accountDeleteModal-id').text(accountId);

    $.get(CONTEXT_PATH + '/account/numberoftransactions/' + accountId, function(data) {
        $('#accountDeleteModalBody').children().hide();

        let deletePossible = true;

        const transCnt = parseInt(data);
        if (data && transCnt === 1) {
            $('#accountDeleteModal-assignedTransactions').show();
            $('#accountDeleteModal-assignedCount').text(transCnt);
            deletePossible = false;
        }

        if ($('#list').children('tr').length === 1) {
            $('#accountDeleteModal-lastAccount').show();
            deletePossible = false;
        }

        if (deletePossible) {
            $('#accountDeleteModal-possible').show();
            submitButton.show();
            submitButton.click(function(event) {
                $.get(CONTEXT_PATH + '/account/delete/' + accountId, function () {
                    window.location.reload();
                });
            });
        }
        else
            submitButton.hide();
    });
});

$('.transaction-count').each(function () {
    const target = $(this);
    const accountId = $(this).data('account-id');
    $.get(CONTEXT_PATH + '/account/numberoftransactions/' + accountId, function (data) {
        target.text(data);
    });
});

$('.import-count').each(function () {
    const target = $(this);
    const accountId = $(this).data('account-id');
    $.get(CONTEXT_PATH + '/account/numberofimports/' + accountId, function (data) {
        target.text(data);
    });
});
