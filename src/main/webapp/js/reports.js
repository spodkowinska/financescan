function init() {
    for (let monthIndex = 0; monthIndex < 12; monthIndex++) {
        $.get('/report/2020/' + (monthIndex + 1), function (data) {
            const month = JSON.parse(data);

            // Fill column with zeros
            $('td.month_' + monthIndex).text('0');

            // Fill given cells with amounts
            for (const cat in month.cats) {
                const id = month.cats[cat].id;
                const balance = month.cats[cat].balance;
                const cell = $('#month_' + monthIndex + '_cat_' + id);
                cell.text(balance);
                cell.addClass(balance > 0 ? 'positive' : 'negative');
            }

            // Fill sum
            {
                const cell = $('#month_'  + monthIndex + '_sum');
                cell.text(month.balance);
                cell.addClass(month.balance > 0 ? 'positive' : 'negative');
            }
        });
    }

    $.get('/report/2020', function (data) {

        const year = JSON.parse(data);

        // Fill sum
        {
            const cell = $('#avg_sum');
            cell.text(year.balance);
            cell.addClass(year.balance > 0 ? 'positive' : 'negative');
        }

        $('.finance_table tbody tr').each(function() {

            // Fill column with avg amount
            const id = $(this).data('category-id');
            const cell = $(this).children().last();
            if (year.cats[id]) {
                const balance = year.cats[id].balance;
                cell.text(balance);
                cell.addClass(balance > 0 ? 'positive' : 'negative');
            }
            else
                cell.text(0);
        });
    });
}
