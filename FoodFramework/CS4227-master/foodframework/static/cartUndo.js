$(document).ready(function() {
  $("#cart_undo").click(function(e) {
    e.preventDefault();
    $.ajax({
      type: "POST",
      contentType: "application/json; charset=utf-8",
      url: "/cart_undo",
      data: $("#cart").val(),
      success: () => {
        window.location.href = "order";
      }
    });
  });
});
