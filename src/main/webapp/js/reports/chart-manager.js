class ChartManager {

    constructor() {
        this.charts = [];
    }

    addChart(chart_class, canvas_id) {
        const chart = new chart_class(canvas_id);
        chart.init();
        this.charts.push(chart);
    }

    beginUpdate() {
        this.charts.forEach(function (chart) {
            chart.freeze(true);
            chart.reset();
        });
    }

    endUpdate() {
        this.charts.forEach(function (chart) {
            chart.update();
            chart.freeze(false);
        });
    }

    reportYear(year) {
        this.charts.forEach(function (chart) {
            chart.onYearReported(chart.config.data.datasets, year);
        });
    }

    reportMonth(month) {
        this.charts.forEach(function (chart) {
            chart.onMonthReported(chart.config.data.datasets, month);
        });
    }
}