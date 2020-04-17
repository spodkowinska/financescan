var gSearchableColumnsIds = [];

var gMonth = "all";
var gYear = "all";

var gUncategorizedCount = 0;
var gUnreviewedCount = 0;
var gSelectedCount = 0;
var gBulkEditEnabled = false;

function init() {
    reloadTransactionTable('all','all');
    gatherSearchableColumnsIds();

    $(document).on('click', '.stop-propagation', function (e) {
        e.stopPropagation();
    })

    $('#bulkEditSwitch').change(function () {
        gBulkEditEnabled = $(this).is(':checked');
        if (gBulkEditEnabled){
            $('.bulk-controls').show();
        }
        else {
            $('.bulk-controls').hide();
            clearSelection();
            $('#onlySelectedCheck').prop('checked', false);
            applyFilters();
        }
    });
}

function gatherSearchableColumnsIds() {
    gSearchableColumnsIds = [];

    let table = document.getElementById("transaction_table");
    let firstRow = table.getElementsByTagName("tr")[0];
    let ths = firstRow.getElementsByTagName("th");
    var offset = 0;

    for (var i = 0; i < ths.length; i++) {
        let th = ths[i];
        if (th) {
            if (th.getAttribute('data-searchable') === 'true')
                gSearchableColumnsIds.push(i + offset);
            let colspan = th.getAttribute('colspan');
            if (colspan) {
                offset += parseInt(colspan) - 1;
            }
        }
    }
}

function enablePopovers(pops) {
    pops.popover();
    pops.on('shown.bs.popover', function () {
        let transId = $(this).data('transaction-id');
        let catId = $(this).data('category-id');

        if (!catId) {
            // Deletion confirmation popover
            if (transId) {
                // Single transaction deletion
                let deleteButton = $('#delete-confirm-' + transId);
                if (deleteButton) {
                    deleteButton.unbind();
                    deleteButton.click(function() {
                        deleteTransaction(transId);
                    });
                }
            }
            else {
                // Selected transactions deletion
                const deleteButton = $('#delete-confirm-selected');
                if (deleteButton) {
                    deleteButton.unbind();
                    deleteButton.click(function() {
                        deleteSelectedTransactions();
                        $('#bulkMenu').hide();
                    });
                }
            }
        }
        else {
            // Category confirmation button
            let catConfirmButton = $('#category-confirm-' + transId + '-' + catId);
            if (catConfirmButton) {
                catConfirmButton.css('color', 'white');
                catConfirmButton.unbind();
                catConfirmButton.click(function () {
                    addCategory(transId, catId);
                });
            }
            // Category rejection button
            let catRejectButton = $('#category-reject-' + transId + '-' + catId);
            if (catRejectButton) {
                catRejectButton.css('color', 'white');
                catRejectButton.unbind();
                catRejectButton.click(function () {
                    removeCategory(transId, catId);
                });
            }
        }
    });

    // This line is needed to prevent category drop-right from disappearing
    $('.tag-add-popover').on("click.bs.dropdown", function (e) { e.stopPropagation(); e.preventDefault(); });
}

function enableLoadingEffect(enable) {
    if (enable) {
        $('#loading-indicator').show();
        $('#list').css('filter', 'grayscale(1) opacity(0.5)');
    }
    else {
        $('#loading-indicator').hide();
        $('#list').css('filter', '');
    }
}

function prepareRows(transactionId) {
    $(transactionId ? ('#cat_row_' + transactionId) : '.finance_table tbody tr').contextmenu(function(e) {
        if (!$(this).hasClass('selected'))
            return false;
        const top = e.pageY;
        const left = e.pageX;
        changeBulkMenuPage('#bulkMenu-mainPage')
        $('#bulkMenu').css({
            display: 'block',
            top: top,
            left: left
        });
        $('#bulkMenuCount').text(gSelectedCount);
        $('#bulkMenuCount2').text(gSelectedCount);
        return false;
    });

    const prefix = transactionId ? '#cat_row_' + transactionId + ' ' : '';
    $(prefix + '.transaction-row-checkbox').change(function () {
        const row = $(this).parent().parent();
        const checked = $(this).is(':checked');
        row.toggleClass('selected', checked);
        gSelectedCount += checked ? 1 : -1;
        refreshFilterBadges();
    });
    if (gBulkEditEnabled)
        $(prefix + '.bulk-controls').show();
}

function toggleSelectionForAllVisibleRows() {
    const checked = $('#master-check').is(':checked');
    const selectedMod = checked ? 1 : -1;
    $('.transaction-row-checkbox').each(function () {
        if (!$(this).is(':hidden') && $(this).is(':checked') !== checked) {
            $(this).prop('checked', checked);
            $(this).parent().parent().toggleClass('selected', checked);
            gSelectedCount += selectedMod;
        }
    });
    refreshFilterBadges();
}

function clearSelection() {
    $('#master-check').prop('checked', false);
    $('.transaction-row-checkbox').each(function () {
        $(this).prop('checked', false);
        $(this).parent().parent().removeClass('selected');
    });
    gSelectedCount = 0;
    refreshFilterBadges();
}

function isTransactionSelected(transactionId) {
    return $('#cat_row_' + transactionId + ' .transaction-row-checkbox').is(':checked');
}

function gatherSelectedRows() {
    const selectedRows = [];
    $('.transaction-row-checkbox').each(function () {
        if ($(this).is(':checked'))
            selectedRows.push($(this).parent().parent());
    });
    return selectedRows;
}

function reloadTransactionTable(year, month) {
    console.log('reloading transaction table');

    enableLoadingEffect(true);

    if (year !== null) {
        gYear = year;
    }
    if (month !== null) {
        gMonth = month;
    }

    $.get(CONTEXT_PATH + "/transaction/table/" + gYear + "/" + gMonth, function (data) {
        $('#list').html(data);

        // Enable popovers
        enablePopovers($('[data-toggle="popover"]'));

        // Some rows maintenance
        prepareRows();

        // Update sorting
        applySorting();

        // Update text and category filtering
        applyFilters();

        enableLoadingEffect(false);
    });

    // Update year & month buttons state
    $('#btn_year_' + gYear).addClass('active').siblings().removeClass('active');

    if (gYear === 'all') {
        // Hide months if all years are displayed
        $('#months').hide('fast');
    }
    else {
        // Show months if only a specific year is displayed
        $('#months').show('fast');

        // Activate only current month button if a specific year is displayed
        $('#btn_month_' + gMonth).addClass('active').siblings().removeClass('active');
    }
}

function deleteTransaction(transId) {
    $.get(CONTEXT_PATH + '/transaction/delete/' + transId);
    let row = $('#cat_row_' + transId);
    if (row.length) {
        if (row.hasClass('selected'))
            gSelectedCount--;
        if (row.data('uncategorized'))
            gUncategorizedCount--;
        if (row.data('unreviewed'))
            gUnreviewedCount--;
        row.remove();
        refreshFilterBadges();
    }
}

function deleteSelectedTransactions() {
    gatherSelectedRows().forEach(function(row) {
        deleteTransaction(row.data('transaction-id'));
    });
}

function refreshRowForTransaction(transactionId, preloadedData){
    const wasSelected = gBulkEditEnabled && isTransactionSelected(transactionId);
    if (preloadedData) {
        $('#cat_row_' + transactionId).replaceWith(preloadedData);
        afterRowRefreshed(transactionId);
        if (wasSelected)
            selectTransaction(transactionId, null, true);
    }
    else {
        $.get(CONTEXT_PATH + "/transaction/gettransaction/" + transactionId, function (data) {
            $('#cat_row_' + transactionId).replaceWith(data);
            afterRowRefreshed(transactionId);
            if (wasSelected)
                selectTransaction(transactionId, null, true);
        })
    }
}

function afterRowRefreshed(transactionId) {
    enablePopovers($('.popover-button-' + transactionId));
    prepareRows(transactionId);
    applyFilters();
}

function selectTransaction(transactionId, event, preserveBulkMenu) {
    if (!gBulkEditEnabled)
        return;

    if (!preserveBulkMenu) {
        const bulkMenu = $('#bulkMenu');
        if (bulkMenu.is(':visible')) {
            bulkMenu.hide();
            return;
        }
    }

    // Ignoring all clicks in delete, edit, categories, etc. Only <td> clicks should pass here.
    if (event == null || event.target.tagName.toUpperCase() === 'TD') {
        const row = $('#cat_row_' + transactionId);

        // Toggle the checkbox
        const checkbox = row.find('input');
        checkbox.trigger('click');
    }
}

function changeAccount(transactionId, accountId) {
    $.get(CONTEXT_PATH + "/transaction/changeaccount/" + transactionId + "/" + accountId, function (data) {
        refreshRowForTransaction(transactionId, data);
    });
}

function changeCategory(transactionId, categoryId) {
    let parentId = $('#cat_tag_' + transactionId + '_' + categoryId).parent().attr('id');

    if (parentId === ('cat_current_' + transactionId))
        removeCategory(transactionId, categoryId);
    else
        addCategory(transactionId, categoryId);
}

function addCategory(transactionId, categoryId) {
    $.get(CONTEXT_PATH + "/transaction/addcategory/" + transactionId + "/" + categoryId, function (data) {
        afterCategoriesChangedForRow(transactionId, data);
    });

    const catElem = $('#cat_tag_' + transactionId + '_' + categoryId);
    const pendingElem = $('#cat_tag_pending_' + transactionId + '_' + categoryId);
    if (pendingElem.length) {
        pendingElem.remove();
        catElem.css('display', 'inline-block');
    }
    else
        catElem.appendTo('#cat_current_' + transactionId);
}

function acceptAllCategories(transactionId) {
    $.get(CONTEXT_PATH + "/transaction/acceptallsuggestions/" + transactionId, function (data) {
        afterCategoriesChangedForRow(transactionId, data + ',0');
    });

    const currentCatsContainer = $('#cat_current_' + transactionId);
    currentCatsContainer.children().filter('.tag-pending').remove();
    currentCatsContainer.children().show();
}

function rejectAllCategories(transactionId) {
    $.get(CONTEXT_PATH + "/transaction/rejectallsuggestions/" + transactionId, function (data) {
        afterCategoriesChangedForRow(transactionId, data + ',0');
    });

    const currentCatsContainer = $('#cat_current_' + transactionId);
    currentCatsContainer.children().filter('.tag-pending').remove();
    currentCatsContainer.children().filter(':hidden').show().appendTo('#cat_others_' + transactionId);
}

function removeAllCategories(transactionId) {
    $.get(CONTEXT_PATH + "/transaction/removeallcategories/" + transactionId, function (data) {
        afterCategoriesChangedForRow(transactionId, '0,0');
    });

    const currentCatsContainer = $('#cat_current_' + transactionId);
    currentCatsContainer.children().filter('.tag-pending').remove();
    currentCatsContainer.children().show().appendTo('#cat_others_' + transactionId);
}

function removeCategory(transactionId, categoryId) {
    $.get(CONTEXT_PATH + "/transaction/removecategory/" + transactionId + "/" + categoryId, function (data) {
        afterCategoriesChangedForRow(transactionId, data);
    });

    let catElem = $('#cat_tag_' + transactionId + '_' + categoryId);
    const pendingElem = $('#cat_tag_pending_' + transactionId + '_' + categoryId);
    if (pendingElem.length) {
        pendingElem.remove();
        catElem.css('display', 'inline-block');
    }
    catElem.appendTo('#cat_others_' + transactionId);
}

function afterCategoriesChangedForRow(transactionId, responseData) {
    const catCnt = responseData.split(",").map(Number);
    const catRowUncategorized = catCnt[0] === 0 && catCnt[1] === 0;
    const catRowUnreviewed = catCnt[1] !== 0;

    const catRow = $('#cat_row_' + transactionId);

    // Uncategorized
    {
        const catRowWasUncategorized = catRow.data('uncategorized');
        if (catRowUncategorized) {
            if (!catRowWasUncategorized) {
                catRow.data('uncategorized', true);
                gUncategorizedCount++;
            }
        }
        else if (catRowWasUncategorized) {
            catRow.data('uncategorized', false);
            gUncategorizedCount--;
        }
    }

    // Unreviewed
    {
        const catRowWasUnreviewed = catRow.data('unreviewed');
        if (catRowUnreviewed) {
            if (!catRowWasUnreviewed) {
                catRow.data('unreviewed', true);
                gUnreviewedCount++;
            }
        }
        else if (catRowWasUnreviewed) {
            catRow.data('unreviewed', false);
            gUnreviewedCount--;
        }
    }

    refreshFilterBadges();
}

function applyFilters() {
    const accountFilter = Number($('#account_filter').val());

    const showOnlyOneAccount = accountFilter !== 0;
    const showOnlyUnreviewed = $('#unreviewedCheck').is(':checked');
    const showOnlyUncategorized = $('#uncategorizedCheck').is(':checked');
    const showOnlySelected = $('#onlySelectedCheck').is(':checked');

    $('#filterRefresh').toggle(showOnlySelected || showOnlyUncategorized || showOnlyUnreviewed || showOnlyOneAccount);

    console.log('accFil',accountFilter,showOnlyOneAccount);

    gUnreviewedCount = 0;
    gUncategorizedCount = 0;
    gSelectedCount = 0;

    const textFilter = $('#text_filter').val().toUpperCase();
    const rows = $('#list tr');

    // Loop through all table rows, and hide those which don't match the search query
    rows.each(function() {
        const row = $(this);
        const cells = row.children('td');

        if (cells.length === 0)
            return;

        let show = true;

        const rowUnreviewed = row.data('unreviewed');
        if (rowUnreviewed) gUnreviewedCount++;

        const rowUncategorized = row.data('uncategorized');
        if (rowUncategorized) gUncategorizedCount++;

        const rowSelected = row.hasClass('selected');
        if (rowSelected) gSelectedCount++;

        if (showOnlySelected && !rowSelected || showOnlyOneAccount && accountFilter !== row.data('account-id')) {
            show = false;
        }
        else if (showOnlyUnreviewed || showOnlyUncategorized) {
            show = showOnlyUnreviewed && rowUnreviewed || showOnlyUncategorized && rowUncategorized;
        }

        if (show && textFilter.length > 2) {
            show = false;

            for (const j of gSearchableColumnsIds) {
                const cell = cells.eq(j);
                if (cell) {
                    const txtValue = cell.attr('sorttable_customkey') || cell.val() || cell.text();
                    if (txtValue.toUpperCase().indexOf(textFilter) > -1) {
                        show = true;
                        break;
                    }
                }
            }
        }

        row.toggle(show);
    });

    refreshFilterBadges();
}

function refreshSelectionBadge() {
    let selectedCount = $('#onlySelectedCount');
    selectedCount.text(gSelectedCount);
}

function refreshFilterBadges() {
    let unreviewedCount = $('#unreviewedCount');
    unreviewedCount.text(gUnreviewedCount);
    if (gUnreviewedCount === 0) {
        unreviewedCount.removeClass('badge-danger');
        unreviewedCount.addClass('badge-secondary');
    }
    else {
        unreviewedCount.removeClass('badge-secondary');
        unreviewedCount.addClass('badge-danger');
    }

    let uncategorizedCount = $('#uncategorizedCount');
    uncategorizedCount.text(gUncategorizedCount);
    if (gUncategorizedCount === 0) {
        uncategorizedCount.removeClass('badge-danger');
        uncategorizedCount.addClass('badge-secondary');
    }
    else {
        uncategorizedCount.removeClass('badge-secondary');
        uncategorizedCount.addClass('badge-danger');
    }

    refreshSelectionBadge();
}

function applySorting() {
    // Get all <th>s from the transaction table
    $('table#transaction_table thead tr th').each(function() {
        // If the <th> has 'sorttable_sorted' then it was sorted.
        // We have to remove the class first to allow sorting on new data.
        if ($(this).hasClass('sorttable_sorted')) {
            $(this).removeClass('sorttable_sorted');
            sorttable.innerSortFunction.apply($(this)[0], []);
        }
            // If the <th> has 'sorttable_sorted_reverse' then it was sorted in reversed order.
            // We have to remove the class first to allow sorting on new data.
            // We have to sort TWICE to achieve reversed order (first is standard).
        // Fortunately the second sort is only a reverse - no actual sorting is performed.
        else if ($(this).hasClass('sorttable_sorted_reverse')) {
            $(this).removeClass('sorttable_sorted_reverse');
            sorttable.innerSortFunction.apply($(this)[0], []);
            sorttable.innerSortFunction.apply($(this)[0], []);
        }
    });
}

function changeBulkMenuPage(pageId) {
    const pageDiv = $(pageId);
    pageDiv.siblings().hide();
    pageDiv.show();
}

$('#editModal').on('show.bs.modal', function(event) {
    // Unbind handlers to avoid situations in which this button has more than one onclick handler
    $('#editModalSubmit').unbind();

    let transId = $(event.relatedTarget).data('transaction-id');

    let link = transId
        // If transId is valid, then we are in edit mode
        ? CONTEXT_PATH + '/transaction/edit/' + transId
        // Otherwise we are in creation mode
        : CONTEXT_PATH + '/transaction/add';

    // Update modal's title depending on mode
    $('#editModalLabel').html(transId ? 'Edit Transaction <span class="small text-gray-500">#' + transId + '</span>' : 'Add New Transaction');

    $.get(link, function(data) {
        $('#editModalBody').html(data);
        $('#editModalSubmit').click(function(event) {
            $('#categories').val(gModalCategories.join(','));
            $('#pendingCategories').val(gModalPendingCategories.join(','));
            $('#rejectedCategories').val(gModalRejectedCategories.join(','));

            $.post(link, $('#editModalForm').serialize(), function(newRowData) {
                if (transId) {
                    // Edit mode: update the edited row
                    $('#cat_row_' + transId).replaceWith(newRowData);
                    afterRowRefreshed(transId);
                }
                else {
                    // Add mode: update whole table
                    reloadTransactionTable(null, null);
                }
            });
        });
    });
});

$('#keywordModal').on('show.bs.modal', function(event) {
    // Unbind handlers to avoid situations in which this button has more than one onclick handler
    $('#keywordModalSubmit').unbind();

    let transId = $(event.relatedTarget).data('transaction-id');
    let getLink = CONTEXT_PATH + '/category/keyword/add/' + transId;
    let postLink = CONTEXT_PATH + '/category/keyword/add';

    $.get(getLink, function(data) {
        $('#keywordModalBody').html(data);
        $('#keywordModalSubmit').click(function(event) {
            $.post(postLink, $('#keywordModalForm').serialize());
        });
    });
});

$('.tag-add-to-selected').click(function () {
    const categoryId = $(this).data('category-id');
    gatherSelectedRows().forEach(function(row) {
        addCategory(row.data('transaction-id'), categoryId);
    })
});

$('.tag-remove-from-selected').click(function () {
    const categoryId = $(this).data('category-id');
    if (categoryId) {
        gatherSelectedRows().forEach(function(row) {
            removeCategory(row.data('transaction-id'), categoryId);
        });
    }
    else {
        gatherSelectedRows().forEach(function(row) {
            removeAllCategories(row.data('transaction-id'));
        });
    }
});

$('#bulkMenu-acceptAllCats').click(function () {
    gatherSelectedRows().forEach(function(row) {
        acceptAllCategories(row.data('transaction-id'));
    })
});

$('#bulkMenu-rejectAllCats').click(function () {
    gatherSelectedRows().forEach(function(row) {
        rejectAllCategories(row.data('transaction-id'));
    })
});

$('#bulkMenu-deselectAll').click(function () {
    clearSelection();
    $('#bulkMenu').hide();
});

$('#bulkMenu-changeAccountPage a').click(function () {
    const accountId = $('#bulkMenu-changeAccountPage select').val();
    gatherSelectedRows().forEach(function(row) {
        changeAccount(row.data('transaction-id'), accountId);
    });
});

$('#bulkMenu').click(function(e) {
    e.stopPropagation();
});

$('#filterRefreshButton').click(function() {
    applyFilters();
});

$(document).click(function () {
    $('#bulkMenu').hide();
}).contextmenu(function(e) {
    $('#bulkMenu').hide();
});
