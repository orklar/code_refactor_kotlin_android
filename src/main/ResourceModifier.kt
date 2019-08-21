package main


import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.JButton
import javax.swing.JLabel
import java.awt.BorderLayout
import javax.swing.JFileChooser
import java.io.File
import javax.swing.filechooser.FileNameExtensionFilter
import java.io.FileOutputStream
import java.lang.Exception


class ResourceModifier {
    //Note: Typically the main method will be in a
    //separate class. As this is a simple one class
    //example it's all in the one class.

    internal var n = 0

    lateinit var fc: JFileChooser
    var selectedFileTOModify: File? = null

    init {

        fc = JFileChooser()
        val guiFrame = JFrame()
        //make sure the program exits when the frame closes
        guiFrame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        guiFrame.title = "Resource Modifier"
        guiFrame.setSize(500, 200)
        //This will center the JFrame in the middle of the screen
        guiFrame.setLocationRelativeTo(null)

        val listPanel = JPanel()

        val fileSelectText = JLabel("Please select a text file that include resources you want to modify:")


        listPanel.add(fileSelectText)

        val fileSelectionButton = JButton("Select File")

        fileSelectionButton.addActionListener {
            chooseFile()
        }
        listPanel.add(fileSelectionButton)

        val starterButton = JButton("Start Modifiying")

        starterButton.addActionListener {
            modifyFile()
        }

        listPanel.add(starterButton)

        guiFrame.add(listPanel, BorderLayout.CENTER)

        guiFrame.isVisible = true

    }

    fun chooseFile() {
        val chooser = JFileChooser()
        val filter = FileNameExtensionFilter(
            "JPG & GIF Images", "jpg", "gif"
        )
        //chooser.fileFilter = filter
        val returnVal = chooser.showOpenDialog(null)
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            println("You chose to open this file: " + chooser.selectedFile.name)
            selectedFileTOModify = chooser.selectedFile

        }
    }

    fun modifyFile() {
        if (selectedFileTOModify != null) {
            val inputBufferMain = StringBuffer()
            val inputBufferSeccondary = StringBuffer()

            var currentHeader = "//none"

            File(selectedFileTOModify?.absolutePath).forEachLine {
                var it=it.replace("\'","\"")
                println(it)
                var finalString = it
                var secondaryIT: String? = null
                if (it.contains("//")) {
                    secondaryIT = '\n'+it
                } else if ((it.contains("implementation") || it.contains("testImplementation")|| it.contains("androidTestImplementation")|| it.contains("compile"))
                    && it.contains(":")&&!it.contains("$")) {
                    try {
                        val versionNumber = getVersionNumber(it)

                        val name = getName(it)

                        secondaryIT = "$name = \"$versionNumber\""

                        finalString = replaceVersionNumberWithVar(it, name)

                        println(finalString)
                        println(secondaryIT)

                    }catch (e: Exception){
                        println("ERROR"+it+e.toString())
                    }

                }
                inputBufferMain.append(finalString);
                inputBufferMain.append('\n');

                if (!secondaryIT.isNullOrBlank()) {
                    inputBufferSeccondary.append(secondaryIT);
                    inputBufferSeccondary.append('\n');
                }
            }

            val inputStrMain = inputBufferMain.toString()
            val fileOut1 = FileOutputStream(selectedFileTOModify!!.absolutePath+"(app)")
            fileOut1.write(inputStrMain.toByteArray(Charsets.UTF_8))
            fileOut1.close()

            val inputStrSecondary = inputBufferSeccondary.toString()
            val fileOut2 = FileOutputStream(selectedFileTOModify!!.absolutePath+"(secondary)")
            fileOut2.write(inputStrSecondary.toByteArray(Charsets.UTF_8))
            fileOut2.close()


            showCompletedDialog()

        } else {
            println("file not selected yet!")
        }
    }

    fun getName(it: String): String {
        val stringwithoutVersionNumberSide = it.substring(0, it.lastIndexOf(":") )
        var nameWithoutEdit= stringwithoutVersionNumberSide.substring(stringwithoutVersionNumberSide.lastIndexOf(":") + 1)
        if(nameWithoutEdit.contains("-")) {
            var lastIndex =0
            while(lastIndex<nameWithoutEdit.length-1){
                val i = nameWithoutEdit.indexOf("-",lastIndex)
                if(i>0)nameWithoutEdit = nameWithoutEdit.substring(0,i)+nameWithoutEdit.get(i+1).toUpperCase()+  nameWithoutEdit.substring(i+2)
                else break
                lastIndex=i+1
            }
            println("fixed name $nameWithoutEdit")
        }
        return nameWithoutEdit
    }

    fun getVersionNumber(it: String): String {
        var startIndexOfVersion = it.lastIndexOf(":")
        var endIndexOfVersion = it.lastIndexOf("\"")
        if (it.contains("@")) endIndexOfVersion = it.lastIndexOf("@")
        return it.substring(startIndexOfVersion+1, endIndexOfVersion)
    }

    fun replaceVersionNumberWithVar(it: String, name: String): String {
        var startIndexOfVersion = it.lastIndexOf(":")
        var endIndexOfVersion = it.lastIndexOf("\"")
        if (it.contains("@")) endIndexOfVersion = it.lastIndexOf("@")
        return it.substring(0,(startIndexOfVersion+1))+"\${rootVersion.$name}"+it.substring(endIndexOfVersion)
    }


    fun showCompletedDialog() {
        val guiFrame = JFrame()
        //make sure the program exits when the frame closes
        guiFrame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        guiFrame.title = "Resource Modifier"
        guiFrame.setSize(200, 200)
        //This will center the JFrame in the middle of the screen
        guiFrame.setLocationRelativeTo(null)

        val listPanel = JPanel()

        val fileSelectText = JLabel("COMPLETED :)")


        listPanel.add(fileSelectText)

        val fileSelectionButton = JButton("EXIT")

        fileSelectionButton.addActionListener {
            System.exit(0);
        }
        listPanel.add(fileSelectionButton)


        guiFrame.add(listPanel, BorderLayout.CENTER)

        guiFrame.isVisible = true

    }


    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            ResourceModifier()
        }
    }
}