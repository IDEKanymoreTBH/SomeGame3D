# SomeGame3D
## Summary:
A Random Game That I May Or May Not Finish

So, This Game I Made. Still Isn't Done. IDK. Maybe You Can Be The One To Pitch In?

## The IAT File Stuff:
So, I Made This Random Thing Where You Can Basically Make A Map Out Of Typing Stuff. It Has Very Rudimental Things Right Now, But Still Has Some Features. Here Are Some:

### Rudimentary Syntax:
First, Each File Must Start With A Document Type Declaration. The Way To Do This Is As Follows:<br></br>
`<DocType DocType.IAT>`<br></br>
Do Note That Failing To Type This EXACT String At The Very Top Of The File Will Result In A Failure.<br></br><br></br>
Also, Each File Must Have A Version Declaration. They Go Like This:<br></br>
`<IATVersion 1.0.0>`<br></br>
Please Type This Exactly. The Interpreter Isn't That Good Right Now :(<br></br><br></br>
Finally, Each File Should Contain A Start And End To The Doc. This Is Done With `#StartDoc` And `#EndDoc`:<br></br>
```
#StartDoc
[Your Code Goes Here]
#EndDoc
```
<br></br>
### Basic Methods:
Right Now, We Really Have Only Three Things:

You Can Create Objects Using This Format:<br></br>
`ObjCreator.create(type, x, y, z, w, h, l);`<br></br>
The Parameters Are As Follows:
- type: The Type To Create. Right Now, Only 'Geometry' Works.
- x: The X Position To Place This Object
- y: The Y Position To Place This Object
- z: The Z Position To Place This Object
- w: The Total Width Of The Object
- h: The Total Height Of The Object
- l: The Total Length Of The Object

<br></br>Another Common Method Is The Print. That Is This:<br></br>
`IATPrintStream.printf(text);`<br></br>
The 'text' Parameter Is The Only One. Text Must Be Circle In Single-Quotes To Work.<br></br><br></br>
The Final Method Is:<br></br>
`IATSystemManager.crash(message);`<br></br>
Here, The 'message' Parameter Is The Message Displayed By The Exception Thrown. Use Single-Quotes.<br></br><br></br>
[NEW] A Method To Use Alongside The Object One Is The Set Texture Method. To Use It, Simply Type:<br></br>
`ObjCreator.setTexture(text);`<br></br>
Here, The Text Parameter Represents The Path To The Texture You Desire. This Must Be Called Before Doing ANY Creation. Blocks Created Before The First Set Texture Will Be Invisible Because The Initial Texture Is Null.
## Patch Notes:
- v0.0.1 Beta:
    - Implemented An Early Map
    - Made Basic Controls
    - Redesigned Everything, Since I Am Not Making SM64 Anymore
- v0.0.2 Beta:
    - Scrapped ToneGod TextFields Because They Are Broken
    - Fixed Game-Kept Patch Notes
    - Added Stamina
- v0.1.0:
    - Wow First Ever Actually Slightly Playable Thing
    - Have Moving Platform For No Reason
    - Flashlight I May Or May Not Remove
    - Added Like 13 Keybinds To The Game
    - Oh BTW It Isn't A Rouge-Like Anymore :)
    - Tons Of Really Shoddy But Sometimes Cool Shadows Rendered
- v0.1.1:
    - Removed Game Patch Notes Tab Because I Have These
    - Also Got Rid Of The PNG For Them (DW All They Had Was v0.0.1 And v0.0.2)
    - No More Code Related To Them
    - I'm Also Realizing That These Version Nums Are Not Actually Realistic. v0.1.0 Was Probably Kept Short, And Could've Had 13 Versions
    - Came Back From Burnout, And Might Continue After I Finish Some Other Projects That I Need To Complete
- v0.1.2:
    - NVM I Can Do More
    - Added Some Methods Related To Blocks
    - Changed The File Structure Slightly
    - Started Working On World Gen With Region Files
- v0.1.3:
    - Fixed Texture Paths For All Asset Users
    - Added Some More Documentation
    - Changed Skybox Color
- v0.1.4:
    - Basically Reworked Ideas
    - Yeah IDEK What The Idea Is ATP Either
- v0.1.5:
    - Added Some Stuff Related To The Multiplayer Maps
    - Edit The Main Menu To Have Everything
    - Added Some Random File Imma Delete Soon
- v0.1.6:
    - Added Functionality To The Buttons
    - The Multiplayer Does Not Work Yet, So It Displays An Alert To Notify You
    - Added Some Textures For Blocks I Think
- v0.1.7:
    - There Is Some More Documentation
    - I Added Some Hands To The GUI
    - [Added An Entire Markdown For A Single File Type](#the-iat-file-stuff)
- v0.1.8:
    - Added Some More Stuff To The IAT File Syntax.
    - Removed The Visual Cursor Because It Was Not Worth It
    - Fixed The Settings Button Hitbox
