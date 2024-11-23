declare function selectCell(params: { name: string, value: string |undefined }[]): void; 
declare function selectMultiCell(params: { name: string, value: string[] }[]): void;

function updateCellSelection(cell: HTMLElement, rowCode: string, columnCode: string): void {
    let tableId = $(cell).closest('.jeesl-datatable');
    let tableIdValue = $(cell).closest('.jeesl-datatable').attr('id');
    tableId.find('.selected').removeClass('selected');

    $(cell).addClass('selected').parent().addClass('selected');

    selectCell([
        { name: 'rowCode', value: rowCode },
        { name: 'columnCode', value: columnCode },
        { name: 'tableId', value: tableIdValue }
    ]);
}

let jeeslCellSelection = {
    lastSelectedCell: undefined as HTMLElement | undefined, 
    single: function(cell: HTMLElement, rowCode: string, columnCode: string): void {
        if (this.lastSelectedCell != undefined)
        {
            $(this.lastSelectedCell).removeClass('selected').parent().removeClass('selected');
        }
        this.lastSelectedCell = cell;
        let tableId = $(cell).closest('.jeesl-datatable');
        let tableIdValue = $(cell).closest('.jeesl-datatable').attr('id');
        tableId.find('.selected').removeClass('selected');
    
        $(cell).addClass('selected').parent().addClass('selected');
    
        selectCell([
            { name: 'rowCode', value: rowCode },
            { name: 'columnCode', value: columnCode },
            { name: 'tableId', value: tableIdValue },
        ]);
    },
    selectedCells: [] as Array<{ name: string, value: string }[]>,
    toRowAndColumnCodes: function (selectedCells: Array<{ name: string, value: string }[]>): Array<{ name: string, value: string[] }> {
        let rowCodes: string[] = [];
        let columnCodes: string[] = [];
        for (let i = 0; i < selectedCells.length; i++) {
            rowCodes.push("{" + selectedCells[i][0].value + "}");
            columnCodes.push("{" + selectedCells[i][1].value + "}");
        }
        return [
            { name: 'rowCodes', value: rowCodes },
            { name: 'columnCodes', value: columnCodes }
        ];
    },
    add: function (cell: HTMLElement, rowCode: string, columnCode: string): void {
        if ($(cell).hasClass('selected'))
        {
            this.remove(cell, rowCode, columnCode);
            return;
        }
        $(cell).addClass('selected').parent().addClass('selected');
        let cellData = [{ name: 'rowCode', value: rowCode },
                        { name: 'columnCode', value: columnCode }];
        this.selectedCells.push(cellData);
        selectMultiCell(this.toRowAndColumnCodes(this.selectedCells));
    },
    remove: function (cell: HTMLElement, rowCode: string, columnCode: string): void {
        let cellData = [{ name: 'rowCode', value: rowCode },
                        { name: 'columnCode', value: columnCode }];
        for (let i = 0; i < this.selectedCells.length; i++)
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
};


// Attach jeeslCellSelection to the global window object
(window as any).jeeslCellSelection = jeeslCellSelection;
(window as any).updateCellSelection = updateCellSelection;