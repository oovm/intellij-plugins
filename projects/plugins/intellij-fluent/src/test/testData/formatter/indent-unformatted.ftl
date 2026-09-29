shared-photos =
        {$userName} {$photoCount ->
                [1] added one photo
                *[other] added {$photoCount} new photos
        } to {$userGender ->
                [male] his stream
                [female] her stream
                *[other] their stream
        }.
