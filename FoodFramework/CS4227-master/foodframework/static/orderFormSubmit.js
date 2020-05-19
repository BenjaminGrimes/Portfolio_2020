$("#order-form").on("submit", e => {
  e.preventDefault();
  const items = $("#order_list")[0].children;
  var cart = $("#cart").val();

  let order_items = {
    items: []
  };
  order_items["items"].push(cart);

  $.ajax({
    type: "POST",
    contentType: "application/json; charset=utf-8",
    url: "/order",
    data: JSON.stringify(order_items),
    success: function(data) {
       window.location.href = "checkout";
    }
  });
});
