/*
    These functions are used to generate the colors for the avatar according to different factors like name.
*/

function stringToColor(str) {
    let hash = 0;
    for (let i = 0; i < str.length; i++) {
      hash = str.charCodeAt(i) + ((hash << 5) - hash);
    }
    let colour = "#";
    for (let i = 0; i < 3; i++) {
      let value = (hash >> (i * 8)) & 0xff;
      colour += ("00" + value.toString(16)).substr(-2);
    }
    return colour;
  }
  
export function stringAvatar(name){
      return {
          sx: {
              bgcolor: stringToColor(name),
          },
          children: name.charAt(0)
      };
  }