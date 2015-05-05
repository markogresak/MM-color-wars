function loadData(callback, samples, n, t) {
  samples = samples || 10;
  n = n || 25;
  t = t || 10;
  $.getJSON(`/data/samples-${samples}-${n}x${n}-t${t}.json`, callback);
}

function makeHistogram(data) {
  window.data = data;
  CanvasJS.addColorSet('graphColors', ['#1FA2CE']);
  // CanvasJS.addColorSet('graphColors', ['#7C078E']);
  histogram = new CanvasJS.Chart('histogramContainer', {
    title: {
      text: 'Percentage over time'
    },
    colorSet: 'graphColors',
    height: 500,
    data: [{
      type: 'column',
      dataPoints: data
    }]
  });
  histogram.render();
}

function updateView() {
  var samples = samplesEl.val();
  var n = sizenEl.val();
  var t = tEl.val();
  loadData(makeHistogram, samples, n, t);
}

$(function() {
  samplesEl = $('#samples');
  sizenEl = $('#sizen');
  tEl = $('#t');
  updateView();
  $('select').change(updateView);
});
