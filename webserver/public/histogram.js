function loadData(callback, samples, n, t) {
  samples = samples || 10;
  n = n || 25;
  t = t || 10;
  $.getJSON(`/data/samples-${samples}-${n}x${n}-t${t}.json`, callback);
}

function makeHistogram(data) {
  CanvasJS.addColorSet('graphColors', ['#1FA2CE']);
  histogram = new CanvasJS.Chart('chartContainer', {
    title: {
      text: 'Delez cez cas'
    },
    colorSet: 'graphColors',
    data: [{
      type: 'column',
      dataPoints: data
    }]
  });
  histogram.render();
}

$(function() {
  loadData(makeHistogram);
});
