class FinanceScanChart {

    /* INTERFACE */

    // MUST BE IMPLEMENTED
    initConfig(config) { console.assert(false, "init_config() not implemented")}

    // MUST BE IMPLEMENTED
    resetConfig(config) { console.assert(false, "reset_config() not implemented") }

    // OPTIONAL
    onYearReported(datasets, year) {}

    // OPTIONAL
    onMonthReported(datasets, month) {}

    /* INTERNALS */

    constructor(canvas_id) {
        this.config = {
            options: {
                responsive: true,
                animation: { duration: 0 },
                layout: { padding: { left: 0, right: 50, top: 0, bottom: 0 } },
                scales: { yAxes: [
                    { ticks: { beginAtZero: true }, afterFit: function (scaleInstance) { scaleInstance.width = 50;} }
                ] }
            }
        };

        this.chart = {};

        const canvas = document.getElementById(canvas_id);
        this.canvas = $(canvas);
        this.context = canvas.getContext('2d');

        console.assert(this.context != null, "Context not properly initialized. Bad canvas id passed?");
    }

    init() {
        this.initConfig(this.config);

        this.resetConfig(this.config);
        this.chart = new Chart(this.context, this.config);
    }

    update() {
        this.chart.update();
    }

    reset() {
        this.resetConfig(this.config);
    }

    freeze(frozen) {
        this.canvas.css('filter', 'opacity(' + (frozen ? '0.5' : '1') + ')');
    }
}
