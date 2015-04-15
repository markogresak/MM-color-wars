function loadData(callback, samples, n, t) {
  samples = samples || 10;
  n = n || 25;
  $.getJSON(`/data/graph-${samples}-${n}x${n}.json`, callback);
}

function makeGraphs(data) {
  var grafiData = [];
  for (var i = 0; i < data.length; i++) {
    grafiData.push({
      type: 'line',
      lineThickness: 1,
      dataPoints: data[i],
      markerType: 'none'
    });
  }

  CanvasJS.addColorSet('graphColors', ['#1FA2CE']);
  // CanvasJS.addColorSet('graphColors', ['#7C078E']);

  chart = new CanvasJS.Chart('grafiContainer', {
    title: {
      text: 'Delež barv'
    },
    colorSet: 'graphColors',
    height: 500,
    axisX: {
      title: 'čas [iteracije]',
      minimum: 0
    },
    axisY: {
      title: 'verjetnost',
      minimum: 0,
      maximum: 1.0
    },
    data: grafiData
  });
  chart.render();
}

function updateView() {
  var samples = samplesEl.val();
  var n = sizenEl.val();
  var t = tEl.val();
  loadData(makeGraphs, samples, n, t);
}

$(function() {
  samplesEl = $('#samples');
  sizenEl = $('#sizen');
  tEl = $('#intervals');
  updateView();
  $('select').change(updateView);
});
