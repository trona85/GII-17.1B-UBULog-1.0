function filter() {
  var input, filter, table, tr, td, i;

  input = document.getElementsByTagName("input");
  table = document.getElementById("myTable");
  tr = table.getElementsByTagName("tr");
  for (i = 1; i < tr.length; i++) {

    td = tr[i].getElementsByTagName("td");
    for (j = 0; j < td.length; j++) {
      console.log(td[j].innerHTML)
      if (td[j].innerHTML.toUpperCase().indexOf(input[j].value.toUpperCase()) > -1) {
        tr[i].style.display = "";
      } else {
        tr[i].style.display = "none";
        break;
      }

    }
  }
}