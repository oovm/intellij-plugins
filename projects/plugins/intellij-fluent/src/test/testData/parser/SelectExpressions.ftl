# Select expression with plural
shared-photos =
  {$userName} {$photoCount ->
     [1] added one photo
    *[other] added {$photoCount} new photos
  }

# Select expression with gender
user-stream =
  {$userGender ->
     [male] his stream
     [female] her stream
    *[other] their stream
  }

# Nested select expressions
complex-message =
  {$userName} {$photoCount ->
     [1] added one photo to {$userGender ->
           [male] his stream
           [female] her stream
          *[other] their stream
         }
    *[other] added {$photoCount} photos to {$userGender ->
           [male] his stream
           [female] her stream
          *[other] their stream
         }
  }
