/*
 * jQuery UI Matrix @VERSION
 *
 * Copyright 2010, AUTHORS.txt (http://jqueryui.com/about)
 * Dual licensed under the MIT or GPL Version 2 licenses.
 * http://jquery.org/license
 *
 * Demo:
 * http://www.vitaligraf.de/en/?Projects:jQuery_plugins:Matrix
 * 
 * Vitali Graf <info@vitaligraf.de>
 *
 * Depends:
 *	jquery.ui.core.js
 *	jquery.ui.widget.js
 */
(function($, undefined) {
	var parentClass = "hasMatrix",
		selfClass = "ui-matrix ui-widget ui-widget-content ui-corner-all",
		headerClass = "ui-matrix-header ui-widget-header ui-helper-clearfix ui-corner-all",
		cellClass = "ui-state-default",
		rtlClass = "ui-matrix-rtl";
	
	_len = function (json){
		var n = 0;
		for (x in json){
			n++;
		}
		return n;
	}
	
	
	$.widget("ui.matrix", {
		options: {
			cols: {}, // REQUIRED: like {ID_1:"label_1", ID_2:"label_2" [, ...], ID_n:"label_n"}
			rows: {}, // REQUIRED: like {ID_1:"label_1", ID_2:"label_2" [, ...], ID_n:"label_n"}
			fillCell: null, // REQUIRED: a function(col_id, row_id){...} that returns a content to using it as argument for jQuery.append(). This function is called for each table cell
			title: "",
			matrixClass: "",
			width: "calculate",
			calculateWidth: 5,
			calculateUnit: "em",
			rtl: false
		},
		_create: function() {
			var self = this,
				o = self.options,
				el = self.element,
				
				n_cols = _len(o.cols) + 1,
				matrixWidth = o.width === "calculate" 
					? "" + n_cols * o.calculateWidth + o.calculateUnit 
					: "" + o.width,
					
				title = o.title === ""
					? el.attr('title')
					: o.title,
				
				tdWith = "" + (100 / n_cols) + "%",
				
				matrix = $('<div/>', {"class": selfClass}),
					
				table = $('<table/>'),
				thead = $('<thead/>'),
				tbody = $('<tbody/>'),
				theadRow = $('<tr/>')
					.append($('<th/>').css({width: tdWith}));
			
			if (o.rtl) {
				matrix.addClass(rtlClass);
			}
			
			if (matrixWidth !== "") {
				matrix.css({width: matrixWidth});
			}
			
			if (title) {
				matrix.append($('<div/>', {"class": headerClass, title: title}).text(title));
			}
			
			if (o.matrixClass !== "") {
				matrix.addClass("" + o.matrixClass);
			}
			
			// fill thead
			for (col in o.cols){
				th = $('<th/>').css({width: tdWith})
					.text(o.cols[col])
					.appendTo(theadRow);
			}
			
			// fill tbody
			for (row in o.rows){
				tr = $('<tr/>');
				td = $('<td/>', {'class': 'th'})
					.text(o.rows[row])
					.appendTo(tr);
				for (col in o.cols){
					td = $('<td/>', {'class': cellClass});
					
					if (typeof o.fillCell === 'function') {
						td.append(o.fillCell(col, row));
					} else {
						td.text('Cell: ' + col + '<br>Row: ' + row);
					}
					
					tr.append(td);
				}
				tbody.append(tr);
			}
			
			thead.append(theadRow);
			table.append(thead)
				.append(tbody);
			matrix.append(table);
			el.append(matrix)
				.addClass(parentClass);
			
			// Do matrix-plugin need it ?
			self._trigger("added", null, matrix); 
		},
		destroy: function() {
			$.Widget.prototype.destroy.call(this);
			this.element
				.removeClass(parentClass)
				.children().last().remove();
		}
	});
})(jQuery); 