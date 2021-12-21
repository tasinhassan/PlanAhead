const functions = require('firebase-functions');
const admin = require('firebase-admin');
const fs=require('fs'); 
const nodemailer = require('nodemailer');

admin.initializeApp(functions.config().firebase);

const gmailEmail = "planaheaddevteam@gmail.com";
const gmailPassword = "planahead2019";
const mailTransport = nodemailer.createTransport({
  service: 'gmail',
  auth: {
    user: gmailEmail,
    pass: gmailPassword,
  },
});

exports.addUser = functions.https.onCall((data) => {
    // Message text passed from the client.
    value = JSON.parse(data);
    const id = value.id;
    const name = value.name;
    const email = value.email;
    return admin.database().ref('/users/' + id).update({
        name: name,
        email: email
      }).then(() => {
        console.log('New User Added');
        // Returning the sanitized message to the client.
        return {name: name, email: email };
      })
      .catch((error) => {
        // Re-throwing the error as an HttpsError so that the client gets the error details.
          throw new functions.https.HttpsError('unknown', error.message, error);
        });
  });

  exports.addEvent = functions.https.onCall((data) => {
    // Message text passed from the client.
    value = JSON.parse(data.event);
    const id = data.eventOwner;
    const eventOwnerName = data.eventOwnerName;
    const eventOwnerEmail = data.eventOwnerEmail;
    const day = value.day;
    const name = value.name;
    const desc = value.description;
    const invitedUsers = value.invitedUsers;
    const time = value.time;
    const minTemp = value.minTemp;
    const maxTemp = value.maxTemp;
    const weatherCondition = value.weatherCondition;
    for(let i = 0; i < invitedUsers.length; i++) {
      admin.auth().getUserByEmail(invitedUsers[i])
      .then(function(userRecord) {
        // See the UserRecord reference doc for the contents of userRecord.
        console.log('Successfully fetched user data:', userRecord.toJSON());
        const invited = userRecord.toJSON().uid;
        admin.database().ref('/users/' + invited + '/notifications/' + day + '/' + name).update({
          day: value.day,
          eventOwnerName: data.eventOwnerName,
          eventOwnerEmail: data.eventOwnerEmail,
          name: value.name,
          desc: value.description,
          invitedUsers: value.invitedUsers,
          time: value.time,
          minTemp: value.minTemp,
          maxTemp: value.maxTemp,
          weatherCondition: value.weatherCondition
        }).then(() => {
          console.log('User(s) Notified');
          // Returning the sanitized message to the client.
          return {day: day };
        })
        .catch((error) => {
          // Re-throwing the error as an HttpsError so that the client gets the error details.
          throw new functions.https.HttpsError('unknown', error.message, error);
        });
      })
      .catch(function(error) {
      console.log('Error fetching user data:', error);
      });
    }
    admin.database().ref('/users/' + id + '/events/' + '/' + day + '/' + name).update({
        day: value.day,
        eventOwnerName: data.eventOwnerName,
        eventOwnerEmail: data.eventOwnerEmail,
        name: value.name,
        desc: value.description,
        invitedUsers: value.invitedUsers,
        time: value.time,
        minTemp: value.minTemp,
        maxTemp: value.maxTemp,
        weatherCondition: value.weatherCondition
      }).then(() => {
        console.log('New Event Added');
        // Returning the sanitized message to the client.
        return {day: day };
      })
      .catch((error) => {
        // Re-throwing the error as an HttpsError so that the client gets the error details.
          throw new functions.https.HttpsError('unknown', error.message, error);
        });
  });

  exports.updateEvent = functions.https.onCall((data) => {
    // Message text passed from the client.
    value = JSON.parse(data.event);
    value2 = JSON.parse(data.oldEvent);
    const id = data.eventOwner;
    const eventOwnerName = data.eventOwnerName;
    const eventOwnerEmail = data.eventOwnerEmail;
    const day = value.day;
    const name = value.name;
    const desc = value.description;
    const newUsers = value.invitedUsers;
    const time = value.time;
    const minTemp = value.minTemp;
    const maxTemp = value.maxTemp;
    const weatherCondition = value.weatherCondition;
    const oldUsers = value2.invitedUsers;
    const newUsersToInvite = [newUsers.length - oldUsers.length];
    const toUpdate = [oldUsers.length];

    newUsers.forEach(val=>{
      if(oldUsers.includes(val))
      {
        toUpdate.push(val)
      }
      else {
        newUsersToInvite.push(val)
      }
    }) 

    if(newUsersToInvite.length != 0) {
      for(var i = 0; i < newUsersToInvite.length; i++) {
        admin.auth().getUserByEmail(newUsersToInvite[i])
        .then(function(userRecord) {
          // See the UserRecord reference doc for the contents of userRecord.
          console.log('Successfully fetched user data:', userRecord.toJSON());
          const invited = userRecord.toJSON().uid;
          admin.database().ref('/users/' + invited + '/notifications/' + day + '/' + name).update({
            day: value.day,
            eventOwnerName: data.eventOwnerName,
            eventOwnerEmail: data.eventOwnerEmail,
            name: value.name,
            desc: value.description,
            invitedUsers: value.invitedUsers,
            time: value.time,
            minTemp: value.minTemp,
            maxTemp: value.maxTemp,
            weatherCondition: value.weatherCondition
          }).then(() => {
            console.log('User(s) Notified');
            // Returning the sanitized message to the client.
            return {day: day };
          })
          .catch((error) => {
            // Re-throwing the error as an HttpsError so that the client gets the error details.
            throw new functions.https.HttpsError('unknown', error.message, error);
          });
        })
        .catch(function(error) {
        console.log('Error fetching user data:', error);
        });
      }
    }
    if(toUpdate.length != 0) {
    for(var i = 0; i < toUpdate.length; i++) {
      admin.auth().getUserByEmail(toUpdate[i])
      .then(function(userRecord) {
        // See the UserRecord reference doc for the contents of userRecord.
        console.log('Successfully fetched user data:', userRecord.toJSON());
        const invited = userRecord.toJSON().uid;
        admin.database().ref('/users/' + invited + '/events/' + day + '/' + name).update({
          day: value.day,
          eventOwnerName: data.eventOwnerName,
          eventOwnerEmail: data.eventOwnerEmail,
          name: value.name,
          desc: value.description,
          invitedUsers: value.invitedUsers,
          time: value.time
        }).then(() => {
          console.log('User(s) Notified');
          // Returning the sanitized message to the client.
          return {day: day };
        })
        .catch((error) => {
          // Re-throwing the error as an HttpsError so that the client gets the error details.
          throw new functions.https.HttpsError('unknown', error.message, error);
        });
      })
      .catch(function(error) {
      console.log('Error fetching user data:', error);
      });
    }
  }
    admin.database().ref('/users/' + id + '/events/' + '/' + day + '/' + name).update({
        day: value.day,
        eventOwnerName: data.eventOwnerName,
        eventOwnerEmail: data.eventOwnerEmail,
        name: value.name,
        desc: value.description,
        invitedUsers: value.invitedUsers,
        time: value.time,
        minTemp: value.minTemp,
        maxTemp: value.maxTemp,
        weatherCondition: value.weatherCondition
      }).then(() => {
        console.log('Updated Event Added');
        // Returning the sanitized message to the client.
        return {day: day };
      })
      .catch((error) => {
        // Re-throwing the error as an HttpsError so that the client gets the error details.
          throw new functions.https.HttpsError('unknown', error.message, error);
        });
  });

  exports.acceptEventInvite = functions.https.onCall((data) => {
    value = JSON.parse(data.event);
    const id = data.id;
    const eventOwner = data.eventOwner;
    const day = value.day;
    const name = value.name;
    const desc = value.desc;
    const time = value.time;
    // Message text passed from the client.
    admin.database().ref('/users/' + id + '/events/' + day + '/' + name).update({
        day: value.day,
        eventOwnerName: data.eventOwnerName,
        eventOwnerEmail: data.eventOwnerEmail,
        name: value.name,
        desc: value.description,
        invitedUsers: value.invitedUsers,
        time: value.time,
        minTemp: value.minTemp,
        maxTemp: value.maxTemp,
        weatherCondition: value.weatherCondition
      }).then(() => {
        admin.database().ref('/users/' + id + '/notifications/' + day + '/' + name).remove()
        console.log('New Event Added');
        // Returning the sanitized message to the client.
        return { day: day };
      })
      .catch((error) => {
        // Re-throwing the error as an HttpsError so that the client gets the error details.
          throw new functions.https.HttpsError('unknown', error.message, error);
        });
  });

  exports.declineEventInvite = functions.https.onCall((data) => {
    value = JSON.parse(data.event);
    const id = data.id;
    const day = value.day;
    const name = value.name;
    const desc = value.desc;
    // Message text passed from the client.
    admin.database().ref('/users/' + id + '/notifications/' + day + '/' + name).remove()({
      }).then(() => {
        console.log('Notification Removed');
        // Returning the sanitized message to the client.
      })
      .catch((error) => {
        // Re-throwing the error as an HttpsError so that the client gets the error details.
          throw new functions.https.HttpsError('unknown', error.message, error);
        });
  });

  exports.removeEvent = functions.https.onCall((data) => {
    value = JSON.parse(data.event);
    const id = data.eventOwner;
    const day = value.day;
    const name = value.name;
    const invitedUsers = value.invitedUsers;
    // Message text passed from the client.
    for(var i = 0; i < invitedUsers.length; i++) {
      admin.auth().getUserByEmail(invitedUsers[i])
      .then(function(userRecord) {
        // See the UserRecord reference doc for the contents of userRecord.
        console.log('Successfully fetched user data:', userRecord.toJSON());
        const invited = userRecord.toJSON().uid;
        admin.database().ref('/users/' + invited + '/notifications/' + day + '/' + name).remove()
        admin.database().ref('/users/' + invited + '/events/' + day + '/' + name).remove()({
        }).then(() => {
          console.log('Event Removed');
          // Returning the sanitized message to the client.
        })
        .catch((error) => {
          // Re-throwing the error as an HttpsError so that the client gets the error details.
          throw new functions.https.HttpsError('unknown', error.message, error);
        });
      })
      .catch(function(error) {
      console.log('Error fetching user data:', error);
      });
    }
    admin.database().ref('/users/' + id + '/events/' + day + '/' + name).remove()({
      }).then(() => {
        console.log('Event Removed');
        // Returning the sanitized message to the client.
      })
      .catch((error) => {
        // Re-throwing the error as an HttpsError so that the client gets the error details.
          throw new functions.https.HttpsError('unknown', error.message, error);
        });
  });

  exports.addTask = functions.https.onCall((data) => {
    // Message text passed from the client.
    value = JSON.parse(data.task);
    const id = data.id;
    const day = value.day;
    const name = value.name;
    const desc = value.description;
    const time = value.time;
    return admin.database().ref('/users/' + id + '/tasks/' + day + '/' + name).update({
        day: value.day,
        name: value.name,
        desc: value.description,
        timeAdded: value.time
      }).then(() => {
        console.log('New Task Added');
        // Returning the sanitized message to the client.
        return {day: day };
      })
      .catch((error) => {
        // Re-throwing the error as an HttpsError so that the client gets the error details.
          throw new functions.https.HttpsError('unknown', error.message, error);
        });
  });

  exports.updateTask = functions.https.onCall((data) => {
    // Message text passed from the client.
    value = JSON.parse(data.task);
    const id = data.id;
    const day = value.day;
    const name = value.name;
    const desc = value.description;
    const time = value.time;
    admin.database().ref('/users/' + id + '/tasks/' + day + '/' + name).remove()
    admin.database().ref('/users/' + id + '/tasks/' + day + '/' + name).update({
      day: value.day,
      name: value.name,
      desc: value.description,
      timeAdded: value.time
      }).then(() => {
        console.log('Updated Task Added');
        // Returning the sanitized message to the client.
        return {day: day };
      })
      .catch((error) => {
        // Re-throwing the error as an HttpsError so that the client gets the error details.
          throw new functions.https.HttpsError('unknown', error.message, error);
        });
  });

  exports.removeTask = functions.https.onCall((data) => {
    value = JSON.parse(data.task);
    const id = data.id;
    const day = value.day;
    const name = value.name;
    // Message text passed from the client.
    admin.database().ref('/users/' + id + '/tasks/' + '/' + day + '/' + name).remove()({
      }).then(() => {
        console.log('Task Removed');
        // Returning the sanitized message to the client.
      })
      .catch((error) => {
        // Re-throwing the error as an HttpsError so that the client gets the error details.
          throw new functions.https.HttpsError('unknown', error.message, error);
        });
  });

  exports.sendWelcomeEmail = functions.auth.user().onCreate((user) => {
    const recipent_email = user.email; 
   
    const mailOptions = {
        from: '"Plan Ahead" <planaheaddevteam@gmail.com>',
        to: recipent_email,
        subject: 'Welcome to PlanAhead',
         html: '<h1> Welcome! </h1> <h2> We are very excited that you have chosen our app. We hope that PlanAhead will prove to be useful to you. </h2> <br> <h2> Cheers, </h2> <h2> PlanAhead Dev Team </2>'
    };
    
  try {
    mailTransport.sendMail(mailOptions);
    console.log('mail send');
    
  } catch(error) {
    console.error('There was an error while sending the email:', error);
  }
return null; 
  });

