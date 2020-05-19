$("#clear_cart_btn").click(function() {
  $("#order_list").empty();
  let total = 0.0;
  document.getElementById("total").value = total;

  $("#order_total").text(total.toFixed(2));
  cart = { cart_items: [] };
  cart_index = 0;
  document.getElementById("cart").value = JSON.stringify(cart);

  $.ajax({
    type: "POST",
    contentType: "application/json; charset=utf-8",
    url: "/save_cart_state",
    data: $("#cart").val(),
    success: () => {
      console.log("success");
    }
  });
});
