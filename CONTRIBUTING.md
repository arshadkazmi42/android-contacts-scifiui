## Contributing Guidelines

## 1. Fork the repo.
Fork this repo by clicking on the fork button on the top of this page.
This will create a copy of this repository in your account.
## 2. Clone repo in your local environment.
Now clone this repo to your machine. Go to your GitHub account, click on the clone button and then click the *copy to clipboard* icon.

Open a terminal and run the following git command:

```
git clone "url you just copied"
```
where "url you just copied" (without the quote marks) is the url to this repository(your fork of this project). See the previous steps to obtain the url.


For example:
```
git clone https://github.com/this-is-you/android-contacts-scifiui.git
```
where `this-is-you` is your GitHub username. Here you're copying the contents of the android-contacts-scifiui repository in GitHub to your computer.
## 3. Run the app in your local environment. 
Follow these steps: [Run app](https://github.com/arshadkazmi42/android-contacts-scifiui#getting-started).

## 4. Checkout a new feature branch.
Change to the repository directory on your computer (if you are not already there):

```
cd android-contacts-scifiui
```
Now create a branch using the `git checkout` command:
```
git checkout -b <add-your-new-branch-name>
```
For example:
```
git checkout -b add-new-feature
```
(The name of the branch does not need to have the word *add* in it, but it's a reasonable thing to include because the purpose of this branch is to add your name to a list.)
## 5. Make changes.
   
If you go to the project directory and execute the command `git status`, you'll see there are changes. 


Add those changes to the branch you just created using the `git add` command:

```
git add filename
```

Now commit those changes using the `git commit` command:
```
git commit -m "commit message"
```
  
## 6. Push the new branch.
Push your changes using the command `git push`:
```
git push origin <add-your-branch-name>
```
replacing `<add-your-branch-name>` with the name of the branch you created earlier.

## 7. Create a pull request.
If you go to your repository on GitHub, you'll see a  `Compare & pull request` button.  Click on that button.

Now submit the pull request.

Soon I'll be merging all your changes into the master branch of this project. You will get a notification email once the changes have been merged.
## 8. Tag the issue, if the pull request is related to a particular issue.

## 9. Delete the branch after pull request has been merged

You can safely delete your branch "<add-your-branch-name>" after the pull request has been merged.
