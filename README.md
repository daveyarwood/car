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

Tell `car` the name of a task and it'll tell you when it's due next. Or, just type `car` on its lonesome and it'll tell you when each of the things are due next.

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
    Oil change: last done at 97500, was due at 105000
    Rotate tires: last done at 96000, was due at 102000

Bingo bango bongo.

If you're ever curious what the interval is between every time you need to do a task, you can do `car <name-of-the-task> interval`:

    $ car 'oil change' interval
    Oil change: due every 7500 miles.

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

The following config options are available when adding a task:

- **last-done**: the last known mile marker at which you did the task
- **interval**: how many miles it will be before the task is due again
- **due-at**: a specific mile marker in the future when the task will be due

> Note: **due-at** is an alternative way to use tasks; it should be used on its own, without an **interval** or **last-done** value set. You should use **due-at** for tasks you only have to do once in a blue moon (say, at 100,000 miles), and **interval**/**last-done** for tasks you do more regularly.

### Editing a task

You can always set `last-done` via the method above (`car <name-of-task> <when-done>` or `car last <name-of-task> <when-done>`.

You can also set `interval` and `due-at` like this:

    $ car oil-change interval 6000
    Oil change: due every 6000 miles.

    $ car rotate-tires due-at 110000
    Rotate tires: last done at 102000, due at 108000.
    # note: we're getting unpredictable results here because 'due-at' is being set on a
    # set on a task that also has values for 'interval' and 'last-done' (see note above)

You can also get rid of values you don't want, if you'd like, by setting them to 0:

    $ car 'oil change' interval 0
    Deleted interval setting for Oil change.

    $ car rotate-tires due-at 0
    Deleted due-at setting for Rotate tires.

### Renaming a task

    $ car rename oil-change 'Change oil'
    Oil change: renamed to Change oil.

### Deleting a task

    $ car delete replace-cabin-air-filter
    Replace cabin air filter: deleted.

    $ car
    Current mileage: 101950
    Oil change: last done at 97500, due at 105000
    Rotate tires: last done at 96000, due at 102000

