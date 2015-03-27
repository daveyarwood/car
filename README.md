# car

A little CLI tool that, when prompted, will tell me the next mile markers when things are due for my car, e.g. oil changes and tire rotations.

## Setup

Copy the example `config.edn` to `~/.config/car/config.edn`:

    $ cp config.edn $HOME/.config/car/config.edn

Edit `~/.config/car/config.edn` to your liking.

Make `car` executable and copy it into `/usr/bin` or whatever:

    $ chmod +x car
    $ cp car /usr/bin

## Usage

The first time you run it, it will ask you for an odometer reading so it can figure out when stuff is due. You could also put this in your `config.edn` as the value for the key `:current`. Or you could do this:

    $ car current 100000
    Current mileage: 100000

(which just stores `:current 100000` in your `config.edn`)

To find out what value is stored as your current mileage at any time, do this:

    $ car current
    Current mileage: 100000

### Checking when things are due

Tell car the name of a task and it'll tell you when it's due next. Or, just type `car` on its lonesome and it'll tell you when each of the things are due next.

    $ car
    Current mileage: 100000
    Oil change: last done at 97500, due at 105000
    Rotate tires: last done at 96000, due at 102000

    $ car oil-change
    Oil change: last done at 97500, due at 105000

    $ car 'ROTATE TIRES'
    Rotate tires: last done at 96000, due at 102000

Oh, but wait. I'm past 102,000 and 105,000 now. It's been a really long time and I forgot to update my current mileage, I'm actually at 260,000 miles now or something ridiculous. wut do i do?

    $ car current 260000
    Current mileage: 260000

    $ car
    Current mileage: 260000
    Oil change: last done at 97500, due at 262500      # fix me (issue #3)
    Rotate tires: last done at 96000, due at 264000    # fix me (issue #3)

Bingo bango bongo.

### Checking/setting when you last did things

To check the last time that you did something:

    $ car last oil-change
    Oil change: last done at 97500

To update the last time you did something:

    $ car last oil-change 106000
    Oil change: last done at 106000

or, if you prefer:

    $ car oil-change 106000
    Oil change: last done at 106000

To see when everything was last done:

    $ car last
    Current mileage: 101701
    Oil change: last done at 97500
    Rotate tires: last done at 96000

### Adding a new task

    $ car add 'Replace cabin air filter' interval=12000 last-done=90000
    Replace cabin air filter: added.

    $ car
    Current mileage: 101950
    Oil change: last done at 97500, due at 105000
    Rotate tires: last done at 96000, due at 102000
    Replace cabin air filter: last done at 90000, due at 102000

### Deleting a task

    $ car delete replace-cabin-air-filter
    Replace cabin air filter: deleted.

    $ car
    Current mileage: 101950
    Oil change: last done at 97500, due at 105000
    Rotate tires: last done at 96000, due at 102000
