# Troubleshoot

* Not able to open eclipse after installation of `BIRT Plugin`.

Run the below code,

```shell
sudo codesign --force --deep --sign - /Applications/Eclipse.app
```