<% class std.name.Class in Rust %>
   class std.name.Class in Rust

<% using std.name.uppercase as A in Rust %>
   using std.name.uppercase as A in Rust

<% for k, v in term if guard %>
   for c in term if guard
    <% if condition %>
       if conditional
    <% else %>
       else
    <% end %>
       end
<% else %>
   else
<% end %>
   end

<<% end %>
