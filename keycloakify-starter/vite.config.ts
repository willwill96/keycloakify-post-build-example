import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";
import { keycloakify } from "keycloakify/vite-plugin";

// https://vitejs.dev/config/
export default defineConfig({
    plugins: [
        react(),
        keycloakify({
            accountThemeImplementation: "none",
            postBuild: async (buildContext) => {
                const fs = await import('fs/promises')
                const path = await import('path')
                const themeFilesDirectory = path.join(buildContext.keycloakifyBuildDirPath, `resources/theme/${buildContext.themeNames[0]}/login`)
                const files = await fs.readdir(themeFilesDirectory)

                for (const file of files) {
                    if (file.endsWith('.ftl')) {
                        const fileLocation = path.join(themeFilesDirectory, file)
                        const fileContents = (await fs.readFile(fileLocation)).toString()
                        let newContents = fileContents
                            // Per Page <title> element Logic
                            .replaceAll(/&lt;\!-- KeycloakifyPageTitle --&gt;/g, file.endsWith('register.ftl') ? "${msg(\"registerTitle\")}" : "${msg(\"loginTitle\",(realm.displayName!''))}")
                        await fs.writeFile(fileLocation, newContents)
                    }
                }
            }
        })
    ]
});
