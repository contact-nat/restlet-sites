Restlet Cloud cells are always in one of the states of the cell lifecycle.

The states of the lifecycle and the possible transitions are shown in the diagram below.

![cell lifecycle](images/cell-lifecycle.jpg "cell lifecycle")

### Draft

This is the initial state.

Major modifications to the cell are allowed.

### Published

The Published state is used to specify that a cell is in a stable state and is ready to be used by end users or imported by other cells.

A Published cell cannot undergo structural modifications or be deleted as this could break clients or other cells using it.

If a Published Data Store is imported by a Published web API, then the Data Store cannot be reverted to Draft state while the API remains Published.

### Deprecated

The Deprecated state is used to specify that a cell will not be supported in the near future, and should no longer be used.

A Deprecated cell cannot undergo structural modifications or be deleted as this could break clients or other cells using it.

If a Deprecated Data Store is imported by a Published web API, it cannot be reverted to Draft state while the API remains Published.

### Archived

Archived state is only used for storage of cells that are no longer used. If you do not wish to delete your cell, you can archive it.

### Deleted

Once a cell is deleted, it no longer exists in any dashboards and is no longer reachable.


>**Note:** Data stores (Entity Stores, Files Stores and Wrappers) are excluded from the cell lifecyle process i.e. they cannot be published, versioned, etc.
