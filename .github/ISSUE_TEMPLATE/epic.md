---
name: Epic
about: 'Template for Epic issues '
title: "[EPIC]"
labels: ''
assignees: ''

---

[Epic title]
=== 

### Introduction

Here, you should include ‘why’ and ‘what’ about the epic.

If you are writing an epic to develop a new feature include why you have decided to develop this feature, what are the user needs you are trying to solve and what are you trying to achieve with this new feature along with the metric you plan to use for measuring the achievement of the new feature. In addition, if you have any documentation or early wireframes for the feature include those in the introduction to provide as much clear picture & information you can provide to your team.

**In short, your introduction can include:**
- Summary of what the features you’re building are for and why you’re building them
- What metrics you are trying to improve
- Links to specific documentation
- Marketing plans, legal requirements (if any)
- Early wireframes

### Product requirements

An essential part of the epic where you provide with an explanation for the whole team working on it to understand what are they going to design, build, test or release. For example, if you are building a feature that the feature has to be fast or should be available in multiple languages, or should work on multiple devices like mobile, tablet and desktop should be mentioned in the product requirement section of your epic.

**Continuing the above example your product requirement could look like below:**
- User can initiate a photo message from the message window
- User can select a photo from their own device
- User can preview the image before sending
- User can cancel the send before it is complete
- User should be able to send any resolution photos from their device
- User should be notified about the successful upload
- User can see the uploaded photo in a preview mode and can click to open a full image

### Design requirements

While writing the design requirement collaborate with your UX designer as much as you can. Take their input as there might be things that a designer thinks is important in order to have a better user experience which wouldn't cross your mind. For example, a designer might think the preview should be of a certain size and the profile picture should always maintain certain resolution in order for a good experience than those kinds of requirement should be written here.

**Continuing the example ahead this can be how your design requirements may look:**
- The photos should be stored in our servers so the user can see them even when they switch their devices
- Maximum photo size should be 2000X2000 pixels.
- Preview should be at least 600X600 in the aspect ratio of the uploaded image
- Loading indicator to be shown to the user while the user is waiting for the upload, in case of any delays
- Success indicator to be shown for successful uploads

### Engineering requirements

Similar to the design requirement in this part of the epic try to involve the engineers or tech lead as much as possible. Their inputs in the early stage will be very useful while estimation and building it correctly. For example, the engineering team might want to build an API to integrate with some other system in order to fetch and maintain the quality of an image, those kinds of specifications and requirements should be mentioned under engineering requirements.

**Continuing with the above example the engineering requirement can look like below:**
- Need a database that can scale to X number of images at a maximum of 5MB per image
- Pull user IDs from the user profile to connect with the photos in our database
- Create an auto-delete system to delete images after 1 year to make it GDPR compliant

---

### Closing remarks:

Having a good epic spec document will have a very positive effect on your team to collaborate and build the right thing. For you as a product manager to create user stories to slice it down and prioritize your backlog, have a smoother development process and easy shipment.

___

For further details about writing an epic, please read the following blog post:
- [How to write Epics and User stories — Best practice](https://productcoalition.com/how-to-write-epics-and-user-stories-best-practice-1de5b983900)
