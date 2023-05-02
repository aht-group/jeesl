function updateCellSelection(cell, rowCode, columnCode) {
	//find cell ancestor with class 'jeesl-datatable'
	var tableId = $(cell).closest('.jeesl-datatable');
	var tableIdValue = $(cell).closest('.jeesl-datatable').attr('id');
	//find cell ancestor with class 'jeesl-datatable' and remove class 'selected'
    tableId.find('.selected').removeClass('selected');

	$(cell).addClass('selected').parent().addClass('selected');

	selectCell([
		{ name: 'rowCode', value: rowCode },
		{ name: 'columnCode', value: columnCode },
		{ name: 'tableId', value: tableIdValue },
	]);
}

var jeeslCellSelection = {
    // empty araaay
    selectedCells: [],
    toRowAndColumnCodes: function (selectedCells) {
        var rowCodes = [];
        var columnCodes = [];
        for (var i = 0; i < selectedCells.length; i++) {
            rowCodes.push("{" + selectedCells[i][0].value + "}");
            columnCodes.push("{" + selectedCells[i][1].value + "}");
        }
        return [
            { name: 'rowCodes', value: rowCodes },
            { name: 'columnCodes', value: columnCodes }
        ];
    },
    add: function (cell, rowCode, columnCode) {
            if ($(cell).hasClass('selected'))
            {
                this.remove(cell, rowCode, columnCode);
                return;
            }
            $(cell).addClass('selected').parent().addClass('selected');
            cellData = [{ name: 'rowCode', value: rowCode },
                        { name: 'columnCode', value: columnCode }];
            this.selectedCells.push(cellData);
            selectMultiCell(this.toRowAndColumnCodes(this.selectedCells));
        },
    remove: function (cell, rowCode, columnCode) {
                cellData = [{ name: 'rowCode', value: rowCode },
                            { name: 'columnCode', value: columnCode }];
                //serach for the index of the object in the array and remove it
               for (var i = 0; i < this.selectedCells.length; i++)
               {
                   if (this.selectedCells[i][0].value == rowCode && this.selectedCells[i][1].value == columnCode)
                   {
                       this.selectedCells.splice(i, 1);
                       $(cell).removeClass('selected').parent().removeClass('selected');
                       selectMultiCell(this.toRowAndColumnCodes(this.selectedCells));
                       break;
                   }
               }
          }
}