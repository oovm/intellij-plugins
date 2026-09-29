# Nested placeables with conditionals
nested =
  {if {$count} > 0 ->
     [true] You have {plural($count, one: "one item", other: "{$count} items")}
    *[false] You have no items
  }.

# Deeply nested placeables
complex-nested =
  Hello, {$userName}! You have {if {$unreadCount} > 0 ->
     [true] {plural($unreadCount, one: "one unread message", other: "{$unreadCount} unread messages")}
    *[false] no unread messages
  } in {NUMBER($folderCount, one: "one folder", other: "{$folderCount} folders")}.

# Nested function calls
nested-functions =
  The total is {NUMBER({ADD($price, $tax)}, currency: "USD")}.
