function init() {
    // Enable popovers
    let pops = $('[data-toggle="popover"]')
    pops.popover();
    pops.on('shown.bs.popover', function () {
        let categoryId = $(this).data('category-id');
        // Deletion confirmation popover
        let deleteButton = $('#delete-confirm-' + categoryId);
        deleteButton.css('color', 'white');
        deleteButton.unbind();
        deleteButton.click(function() {
            deleteCategory(categoryId);
        });
    });
}

function deleteCategory(categoryId) {
    console.log(categoryId);
    window.location.href = CONTEXT_PATH + '/category/delete/' + categoryId;
}

function reloadCategoryTable() {
    console.log('reload');
    window.location.reload();
}

// https://stackoverflow.com/a/5624139/1550934
function hexToRgb(hex) {
    let result = /^#?([a-f\d]{2})([a-f\d]{2})([a-f\d]{2})$/i.exec(hex);
    return result ? {
        r: parseInt(result[1], 16),
        g: parseInt(result[2], 16),
        b: parseInt(result[3], 16)
    } : null;
}

// https://stackoverflow.com/a/1855903/1550934
function calculateContrastingColor(hex) {
    let rgb = hexToRgb(hex);
    // Counting the perceptive luminance - human eye favors green color...
    let luminance = ( 0.299 * rgb.r + 0.587 * rgb.g + 0.114 * rgb.b)/255;
    return luminance > 0.5 ? '#000000' : '#FFFFFF';
}

function updateColor(categoryId) {
    let color = $('#category_color_' + categoryId).val();
    let fontColor = calculateContrastingColor(color);
    $.post(CONTEXT_PATH + '/category/setcolor/' + categoryId, { 'color' : color, 'fontColor' : fontColor }, function () {
        reloadCategoryTable();
    });
}

$('#categoryModal').on('show.bs.modal', function(event) {
    // Unbind handlers to avoid situations in which this button has more than one onclick handler
    $('#categoryModalSubmit').unbind();

    let categoryId = $(event.relatedTarget).data('category-id');
    let link = categoryId
        // If categoryId is valid, then we are in edit mode
        ? CONTEXT_PATH + '/category/edit/' + categoryId
        // Otherwise we are in creation mode
        : CONTEXT_PATH + '/category/add';

    // Update modal's title depending on mode
    $('#categoryModalLabel').html(categoryId ? 'Edit Category <span class="small text-gray-500">#' + categoryId + '</span>' : 'Add New Category');

    $.get(link, function(data) {
        $('#categoryModalBody').html(data);
        $('#categoryModalSubmit').click(function(event) {
            // Find all keyword inputs and merge them before submitting
            let keywords = [], safeKeywords = [];
            $('#keywordList .keyword-text').each(function() {
                let val = $(this).val();
                if (val && val !== "") {
                    let keywordNumber = $(this).data('keyword-number');
                    if ($('#safe-keyword-check-' + keywordNumber).is(':checked'))
                        safeKeywords.push(val);
                    else
                        keywords.push(val);
                }
            });
            $('#keywords').val(keywords.join(','));
            $('#safeKeywords').val(safeKeywords.join(','));

            // Submit
            $.post(link, $('#categoryModalForm').serialize(), function () {
                reloadCategoryTable();
            });
        });
    });
});

$('#categoryDeleteModal').on('show.bs.modal', function(event) {
    // Unbind handlers to avoid situations in which this button has more than one onclick handler
    $('#categoryDeleteModalSubmit').unbind();

    let categoryId = $(event.relatedTarget).data('category-id');

    $('#categoryDeleteModal-id').text(categoryId);

    $.get(CONTEXT_PATH + '/category/numberoftransactions/' + categoryId, function(data) {
        const transCnt = data.split(',').map(Number);
        const transAssignedCnt = transCnt[0];
        const transPendingCnt = transCnt[1];

        if (transAssignedCnt > 0) {
            $('#categoryDeleteModal-assigned').show();
            $('#categoryDeleteModal-noAssigned').hide();
            $('#categoryDeleteModal-assignedCount').text(transAssignedCnt);
        }
        else {
            $('#categoryDeleteModal-assigned').hide();
            $('#categoryDeleteModal-noAssigned').show();
        }

        if (transPendingCnt > 0) {
            $('#categoryDeleteModal-unreviewed').show();
            $('#categoryDeleteModal-noUnreviewed').hide();
            $('#categoryDeleteModal-unreviewedCount').text(transPendingCnt);
        }
        else {
            $('#categoryDeleteModal-unreviewed').hide();
            $('#categoryDeleteModal-noUnreviewed').show();
        }

        if (transAssignedCnt === 0 && transPendingCnt === 0) {
            $('#categoryDeleteModal-conclusion-safe').show();
            $('#categoryDeleteModal-conclusion-decide').hide();
        }
        else {
            $('#categoryDeleteModal-conclusion-safe').hide();
            $('#categoryDeleteModal-conclusion-decide').show();
        }

        // Submit
        $('#categoryDeleteModalSubmit').click(function(event) {
            $.get(CONTEXT_PATH + '/category/delete/' + categoryId, function () {
                reloadCategoryTable();
            });
        });
    });
});
