function loadData(callback, samples, n, t) {
  samples = samples || 10;
  n = n || 25;
  t = t || 10;
  $.getJSON(`/data/samples-${samples}-${n}x${n}-t${t}.json`, callback);
}

function makeHistogram(data) {
  window.data = data;
  CanvasJS.addColorSet('graphColors', ['#1FA2CE']);
  histogram = new CanvasJS.Chart('histogramContainer', {
    title: {
      text: 'Delez cez cas'
    },
    colorSet: 'graphColors',
    height: 500,
    axisX: {
      minimum: 0
    },
    axisY: {
      minimum: 0
    },
    data: [{
      type: 'column',
      dataPoints: data
    }]
  });
  histogram.render();
}

$(function() {
  loadData(makeHistogram, 500, 50);
});
